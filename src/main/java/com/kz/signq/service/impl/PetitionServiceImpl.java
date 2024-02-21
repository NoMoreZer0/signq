package com.kz.signq.service.impl;

import com.kz.signq.db.PetitionDb;
import com.kz.signq.dto.EntityIdDto;
import com.kz.signq.dto.PetitionDto;
import com.kz.signq.dto.PetitionsDto;
import com.kz.signq.model.Petition;
import com.kz.signq.model.User;
import com.kz.signq.service.ImageService;
import com.kz.signq.service.PetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PetitionServiceImpl implements PetitionService {

    private final PetitionDb db;

    private final ImageService imageService;

    @Autowired
    public PetitionServiceImpl(PetitionDb db, ImageService imageService) {
        this.db = db;
        this.imageService = imageService;
    }

    @Override
    public PetitionsDto getCreatedPetitions(User user) {
        var petitions = db.findAllByCreatedBy(user.getId());
        return PetitionsDto.builder()
                .petitions(petitions)
                .build();
    }

    @Override
    public List<Petition> getAll() {
        return db.findAll();
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
}
