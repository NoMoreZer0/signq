package com.kz.signq.service;

import com.kz.signq.dto.MessageDto;

import java.util.UUID;

public interface PetitionStatusService {

    void init(UUID petitionId);

    MessageDto process(UUID petitionId);

    MessageDto reject(UUID petitionId);
}
