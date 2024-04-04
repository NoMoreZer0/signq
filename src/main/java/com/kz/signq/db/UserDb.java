package com.kz.signq.db;

import com.kz.signq.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDb extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String email);
}
