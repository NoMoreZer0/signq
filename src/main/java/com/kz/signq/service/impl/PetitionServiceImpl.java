package com.kz.signq.service.impl;

import com.kz.signq.db.PetitionDb;
import com.kz.signq.dto.EntityIdDto;
import com.kz.signq.dto.MessageDto;
import com.kz.signq.dto.eds.EdsDto;
import com.kz.signq.dto.petition.PetitionDto;
import com.kz.signq.dto.petition.PetitionsDto;
import com.kz.signq.dto.petition.response.PetitionResponseDto;
import com.kz.signq.dto.signature.SignXmlDto;
import com.kz.signq.exception.EntityNotFoundException;
import com.kz.signq.exception.ErrorCodeException;
import com.kz.signq.exception.PetitionNotFoundException;
import com.kz.signq.exception.SignException;
import com.kz.signq.model.*;
import com.kz.signq.service.FileService;
import com.kz.signq.service.PetitionService;
import com.kz.signq.service.PetitionStatusService;
import com.kz.signq.service.SignatureService;
import com.kz.signq.utils.ErrorCodeUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PetitionServiceImpl implements PetitionService {

    private final PetitionDb db;

    private final SignatureService signatureService;

    private final FileService fileService;

    private final PetitionStatusService petitionStatusService;

    private static final String PETITION_NOT_FOUND_MSG = "petition not found!";

    private static final String IMAGE_NOT_FOUND_MSG = "image not found!";

    private static final List<PetitionStatus> statuses = List.of(PetitionStatus.PUBLISH, PetitionStatus.ACCEPT); // List of statuses for publishing

    @Override
    @Transactional
    public PetitionsDto getCreatedPetitions(User user) {
        var petitions = db.findAllByCreatedBy(user.getId());
        return PetitionsDto.builder()
                .petitions(petitions)
                .build();
    }

    @Override
    @Transactional
    public List<PetitionResponseDto> getAll(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        var petitions = db.findAllByStatusIn(statuses, pageable);
        var allPetitions = new ArrayList<PetitionResponseDto>();
        petitions.forEach(petition -> allPetitions.add(
                PetitionResponseDto.fromPetition(petition)
        ));
        return allPetitions;
    }

    @Override
    public PetitionResponseDto getById(UUID id) {
        var opt = db.findById(id);
        if (opt.isEmpty()) {
            throw new ErrorCodeException("ERR_PETITION_NOT_FOUND", "petition not found");
        }
        var petition = PetitionResponseDto.fromPetition(opt.get());
        petition.setSignedCount(signatureService.countSignaturesForPetition(id));
        return petition;
    }

    @Override
    public EntityIdDto create(PetitionDto petitionDto) {
        var file = fileService.findById(petitionDto.getFileId());
        var petition = Petition.builder()
                .file(file.orElse(null))
                .title(petitionDto.getTitle())
                .body(petitionDto.getBody())
                .agency(petitionDto.getAgency())
                .build();
        petition = db.save(petition);
        petitionStatusService.init(petition.getId());
        return EntityIdDto.fromBaseEntity(
                petition
        );
    }

    @Override
    public EntityIdDto update(PetitionDto petitionDto, UUID petitionId) throws EntityNotFoundException {
        var opt = db.findById(petitionId);
        if (opt.isEmpty()) {
            throw new EntityNotFoundException(
                    ErrorCodeUtil.ERR_ENTITY_NOT_FOUND.name(),
                    PETITION_NOT_FOUND_MSG
            );
        }
        var petition = opt.get();
        File image = null;
        if (petitionDto.getFileId() != null) {
            var optFile = fileService.findById(petitionDto.getFileId());
            if (optFile.isEmpty()) {
                throw new EntityNotFoundException(
                        ErrorCodeUtil.ERR_ENTITY_NOT_FOUND.name(),
                        IMAGE_NOT_FOUND_MSG
                );
            }
            image = optFile.orElse(null);
        }
        petition.updateByDto(petitionDto, image);
        return EntityIdDto.fromBaseEntity(
                db.save(petition)
        );
    }

    @Override
    public PetitionsDto findSignedPetitions(User user) {
        if (user.getIin() == null) {
            return toPetitionsDto(List.of());
        }
        var signatures = signatureService.getAllSigned(user.getIin());
        return toPetitionsDto(signatures);
    }

    @Override
    public MessageDto signEds(EdsDto dto, User user) throws PetitionNotFoundException, SignException, NoSuchAlgorithmException {
        var opt = db.findById(dto.getPetitionId());
        if (opt.isEmpty()) {
            throw new PetitionNotFoundException(
                    ErrorCodeUtil.ERR_PETITION_NOT_FOUND.name(),
                    PETITION_NOT_FOUND_MSG
            );
        }
        var petition = opt.get();
        var certificateStore = dto.getCertificateStore();
        var password = dto.getPassword();
        byte[] dataSnapshot = signatureService.createDataSnapshot(petition);
        signatureService.checkCertificate(user, petition, dataSnapshot, certificateStore, password);
        signatureService.signApplication(user, petition, dataSnapshot, certificateStore, password);
        petitionStatusService.process(dto.getPetitionId());
        return MessageDto.builder().msg("signed successfully!").build();
    }

    @Override
    public MessageDto signXml(SignXmlDto dto, User user) throws SignException {
        var signature = signatureService.checkCertificateXml(user, dto.getPetitionId(), dto.getXml());
        var petition = db.findById(dto.getPetitionId()).orElse(null);
        signatureService.saveSignatureXml(user.getIin(), dto.getPetitionId(), signature);
        if (petition != null && petition.getCreatedBy().equals(user.getId())) {
            petitionStatusService.process(dto.getPetitionId());
        }
        return MessageDto.builder()
                .msg("Успешно подписано")
                .build();
    }

    @Override
    public List<Petition> getReviewPetitions(int page, int size, String status, String sortBy, String orderBy) {
        Pageable pageable;
        if (orderBy.equals("desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        }

        if (status.isEmpty()) {
            return db.findAll(pageable).stream().toList();
        }

        return db.findAllByStatus(PetitionStatus.valueOf(status), pageable);
    }

    private PetitionsDto toPetitionsDto(List<DigitalSignature> signatures) {
        var petitions = new ArrayList<Petition>();
        signatures.forEach(signature -> {
            var p = db.findById(signature.getPetitionId());
            p.ifPresent(petitions::add);
        });
        return PetitionsDto.builder()
                .petitions(petitions)
                .build();
    }
}
