package com.kz.signq.service;

import java.util.UUID;

public interface PetitionStatusService {

    void init(UUID petitionId);

    void process(UUID petitionId);

    void reject(UUID petitionId);
}
