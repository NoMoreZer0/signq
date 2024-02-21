package com.kz.signq.db;

import com.kz.signq.model.Petition;
import com.kz.signq.model.User;
import com.kz.signq.model.UserPetitionSign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserPetitionSignDb extends JpaRepository<UserPetitionSign, UUID> {

    List<UserPetitionSign> findAllByUser(User user);
    boolean existsByUserAndPetition(User user, Petition petition);
}
