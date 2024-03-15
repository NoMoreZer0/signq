package com.kz.signq.service.impl;

import com.kz.signq.db.PetitionDb;
import com.kz.signq.dto.EntityIdDto;
import com.kz.signq.dto.petition.PetitionDto;
import com.kz.signq.dto.petition.response.PetitionResponseDto;
import com.kz.signq.dto.petition.PetitionsDto;
import com.kz.signq.exception.PetitionAlreadySignedByUserException;
import com.kz.signq.exception.PetitionNotFoundException;
import com.kz.signq.model.Petition;
import com.kz.signq.model.User;
import com.kz.signq.model.UserPetitionSign;
import com.kz.signq.service.ImageService;
import com.kz.signq.service.PetitionService;
import com.kz.signq.service.UserPetitionSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PetitionServiceImpl implements PetitionService {

    private final PetitionDb db;

    private final ImageService imageService;

    private final UserPetitionSignService signService;

    @Autowired
    public PetitionServiceImpl(PetitionDb db, ImageService imageService, UserPetitionSignService signService) {
        this.db = db;
        this.imageService = imageService;
        this.signService = signService;
    }

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
                user.getId().equals(petition.getCreatedBy());
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
       // var image = imageService.findById(petitionDto.getImageId());
        var petition = Petition.builder()
         //       .img(image.get())
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
            throw new PetitionNotFoundException("Petition not found!");
        }
        signService.save(user, petition.get());
        return "signed successfully!";
    }

    @Override
    public PetitionsDto findSignedPetitions(User user) {
        var userPetitions = signService.findAllSigned(user);
        return toPetitionsDto(userPetitions);
    }

    @Override
    public PetitionResponseDto isMyPetition(User user, EntityIdDto dto) throws PetitionNotFoundException {
        var opt = db.findById(dto.getId());
        if (opt.isEmpty()) {
            throw new PetitionNotFoundException("petition not found");
        }
        var petition = opt.get();
        return PetitionResponseDto.builder()
                .isOwner(user.getId().equals(petition.getCreatedBy()))
                .build();
    }

    private PetitionsDto toPetitionsDto(List<UserPetitionSign> signs) {
        var petitions = new ArrayList<Petition>();
        signs.forEach(sign -> petitions.add(sign.getPetition()));
        return PetitionsDto.builder()
                .petitions(petitions)
                .build();
    }
}
