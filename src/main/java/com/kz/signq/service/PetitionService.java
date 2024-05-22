package com.kz.signq.service;

import com.kz.signq.dto.EntityIdDto;
import com.kz.signq.dto.MessageDto;
import com.kz.signq.dto.eds.EdsDto;
import com.kz.signq.dto.petition.PetitionDto;
import com.kz.signq.dto.petition.PetitionsDto;
import com.kz.signq.dto.petition.response.PetitionResponseDto;
import com.kz.signq.dto.signature.SignXmlDto;
import com.kz.signq.exception.EntityNotFoundException;
import com.kz.signq.exception.PetitionNotFoundException;
import com.kz.signq.exception.SignException;
import com.kz.signq.model.Petition;
import com.kz.signq.model.User;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PetitionService {

    List<PetitionResponseDto> getAll(User user, int page, int size);

    Optional<Petition> getById(UUID id);

    EntityIdDto create(PetitionDto petitionDto);

    EntityIdDto update(PetitionDto petitionDto, UUID petitionId) throws EntityNotFoundException;

    PetitionsDto getCreatedPetitions(User user);

    PetitionsDto findSignedPetitions(User user);

    PetitionResponseDto isMyPetition(User user, EntityIdDto dto) throws PetitionNotFoundException;

    MessageDto signEds(EdsDto dto, User user) throws PetitionNotFoundException, SignException, NoSuchAlgorithmException;

    MessageDto signXml(SignXmlDto dto, User user) throws SignException;
}
