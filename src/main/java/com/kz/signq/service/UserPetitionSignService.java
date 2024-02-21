package com.kz.signq.service;

import com.kz.signq.exception.PetitionAlreadySignedByUserException;
import com.kz.signq.model.Petition;
import com.kz.signq.model.User;
import com.kz.signq.model.UserPetitionSign;

import java.util.List;

public interface UserPetitionSignService {

    void save(User user, Petition petition) throws PetitionAlreadySignedByUserException;

    List<UserPetitionSign> findAllSigned(User user);
}
