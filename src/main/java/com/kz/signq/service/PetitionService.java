package com.kz.signq.service;

import com.kz.signq.dto.EntityIdDto;
import com.kz.signq.dto.MessageDto;
import com.kz.signq.dto.eds.EdsDto;
import com.kz.signq.dto.petition.PetitionDto;
import com.kz.signq.dto.petition.response.PetitionResponseDto;
import com.kz.signq.dto.petition.PetitionsDto;
import com.kz.signq.exception.PetitionAlreadySignedByUserException;
import com.kz.signq.exception.PetitionNotFoundException;
import com.kz.signq.exception.SignException;
import com.kz.signq.model.Petition;
import com.kz.signq.model.User;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PetitionService {

    List<PetitionResponseDto> getAll(User user);

    Optional<Petition> getById(UUID id);

    EntityIdDto save(PetitionDto petitionDto);

    PetitionsDto getCreatedPetitions(User user);

    String sign(User user, EntityIdDto entityIdDto) throws PetitionAlreadySignedByUserException, PetitionNotFoundException;

    PetitionsDto findSignedPetitions(User user);

    PetitionResponseDto isMyPetition(User user, EntityIdDto dto) throws PetitionNotFoundException;

    MessageDto signEds(EdsDto dto, User user) throws PetitionNotFoundException, SignException, NoSuchAlgorithmException;
}
