package com.kz.signq.db;

import com.kz.signq.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDb extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
}
