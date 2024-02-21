package com.kz.signq.service;

import com.kz.signq.dto.EntityIdDto;
import com.kz.signq.dto.PetitionDto;
import com.kz.signq.dto.PetitionsDto;
import com.kz.signq.exception.PetitionAlreadySignedByUserException;
import com.kz.signq.exception.PetitionNotFoundException;
import com.kz.signq.model.Petition;
import com.kz.signq.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PetitionService {

    List<Petition> getAll();

    Optional<Petition> getById(UUID id);

    EntityIdDto save(PetitionDto petitionDto);

    PetitionsDto getCreatedPetitions(User user);

    String sign(User user, EntityIdDto entityIdDto) throws PetitionAlreadySignedByUserException, PetitionNotFoundException;

    PetitionsDto findSignedPetitions(User user);
}
