package com.kz.signq.service.impl;

import com.kz.signq.db.UserPetitionSignDb;
import com.kz.signq.exception.PetitionAlreadySignedByUserException;
import com.kz.signq.model.Petition;
import com.kz.signq.model.User;
import com.kz.signq.model.UserPetitionSign;
import com.kz.signq.service.UserPetitionSignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserPetitionSignServiceImpl implements UserPetitionSignService {

    private final UserPetitionSignDb db;

    @Autowired
    public UserPetitionSignServiceImpl(UserPetitionSignDb db) {
        this.db = db;
    }

    @Override
    public void save(User user, Petition petition) throws PetitionAlreadySignedByUserException {
        log.info("{} trying to sign petition {}", user, petition);
        if (db.existsByUserAndPetition(user, petition)) {
            log.error("Petition {} already signed by the user", petition);
            throw new PetitionAlreadySignedByUserException("Petition already signed by the user");
        }
        var userPetitionSign = UserPetitionSign.builder()
                                .petition(petition)
                                .user(user)
                                .build();
        db.save(userPetitionSign);
    }

    @Override
    public List<UserPetitionSign> findAllSigned(User user) {
        return db.findAllByUser(user);
    }
}
