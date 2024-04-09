package com.kz.signq.service.impl;

import com.kz.signq.db.PetitionDb;
import com.kz.signq.dto.EntityIdDto;
import com.kz.signq.dto.MessageDto;
import com.kz.signq.dto.eds.EdsDto;
import com.kz.signq.dto.petition.PetitionDto;
import com.kz.signq.dto.petition.PetitionsDto;
import com.kz.signq.dto.petition.response.PetitionResponseDto;
import com.kz.signq.exception.PetitionAlreadySignedByUserException;
import com.kz.signq.exception.PetitionNotFoundException;
import com.kz.signq.exception.SignException;
import com.kz.signq.model.DigitalSignature;
import com.kz.signq.model.Petition;
import com.kz.signq.model.User;
import com.kz.signq.service.*;
import com.kz.signq.utils.ErrorCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PetitionServiceImpl implements PetitionService {

    private final PetitionDb db;

    private final UserPetitionSignService signService;

    private final SignatureService signatureService;

    private final FileService fileService;

    private static final String PETITION_NOT_FOUND_MSG = "petition not found!";

    @Override
    public PetitionsDto getCreatedPetitions(User user) {
        var petitions = db.findAllByCreatedBy(user.getId());
        return PetitionsDto.builder()
                .petitions(petitions)
                .build();
    }

    @Override
    public List<PetitionResponseDto> getAll(User user) {
        var petitions = db.findAll();
        var allPetitions = new ArrayList<PetitionResponseDto>();
        petitions.forEach(petition -> {
            var isOwner = false;
            if (user != null) {
                isOwner = user.getId().equals(petition.getCreatedBy());
            }
            allPetitions.add(
                    PetitionResponseDto.fromPetition(petition, isOwner)
            );
        });
        return allPetitions;
    }

    @Override
    public Optional<Petition> getById(UUID id) {
        return db.findById(id);
    }

    @Override
    public EntityIdDto save(PetitionDto petitionDto) {
        var file = fileService.findById(petitionDto.getFileId());
        var petition = Petition.builder()
                .file(file.orElse(null))
                .title(petitionDto.getTitle())
                .body(petitionDto.getBody())
                .agency(petitionDto.getAgency())
                .build();
        return EntityIdDto.fromBaseEntity(
                db.save(petition)
        );
    }

    @Override
    public String sign(User user, EntityIdDto entityIdDto) throws PetitionAlreadySignedByUserException, PetitionNotFoundException {
        var petition = db.findById(entityIdDto.getId());
        if (petition.isEmpty()) {
            throw new PetitionNotFoundException(
                    ErrorCodeUtil.ERR_PETITION_NOT_FOUND.name(),
                    PETITION_NOT_FOUND_MSG
            );
        }
        signService.save(user, petition.get());
        return "signed successfully!";
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
    public PetitionResponseDto isMyPetition(User user, EntityIdDto dto) throws PetitionNotFoundException {
        var opt = db.findById(dto.getId());
        if (opt.isEmpty()) {
            throw new PetitionNotFoundException(
                    ErrorCodeUtil.ERR_PETITION_NOT_FOUND.name(),
                    PETITION_NOT_FOUND_MSG
            );
        }
        var petition = opt.get();
        return PetitionResponseDto.builder()
                .isOwner(user.getId().equals(petition.getCreatedBy()))
                .build();
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
        return MessageDto.builder().msg("signed successfully!").build();
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
