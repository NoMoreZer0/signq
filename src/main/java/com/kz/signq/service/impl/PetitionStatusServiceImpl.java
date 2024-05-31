package com.kz.signq.service.impl;

import com.kz.signq.db.PetitionDb;
import com.kz.signq.dto.MessageDto;
import com.kz.signq.model.PetitionStatus;
import com.kz.signq.service.PetitionStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetitionStatusServiceImpl implements PetitionStatusService {

    private final PetitionDb db;

    @Override
    public void init(UUID petitionId) {
        var petition = db.findById(petitionId).orElse(null);
        if (petition != null) {
            petition.setStatus(PetitionStatus.DRAFT);
            db.save(petition);
        }
    }

    @Override
    public MessageDto process(UUID petitionId) {
        var petition = db.findById(petitionId).orElse(null);
        if (petition != null) {
            switch (petition.getStatus()) {
                case DRAFT -> petition.setStatus(PetitionStatus.ON_REVIEW);
                case ON_REVIEW -> petition.setStatus(PetitionStatus.PUBLISH);
                case ON_FINAL_REVIEW -> petition.setStatus(PetitionStatus.ACCEPT);
                default -> log.info("no action process: {}", petition.getStatus());
            }
            db.save(petition);
        }
        return MessageDto.builder()
                .msg("SUCCESS!")
                .build();
    }

    @Override
    public MessageDto reject(UUID petitionId) {
        var petition = db.findById(petitionId).orElse(null);
        if (petition != null) {
            switch (petition.getStatus()) {
                case ON_REVIEW -> petition.setStatus(PetitionStatus.DISAPPROVED);
                case ON_FINAL_REVIEW -> petition.setStatus(PetitionStatus.REJECT);
                default -> log.info("no action process: {}", petition.getStatus());
            }
            db.save(petition);
        }
        return MessageDto
                .builder()
                .msg("REJECT!")
                .build();
    }
}
