package com.kz.signq.service;

import com.kz.signq.exception.EmailAlreadyExistsException;
import com.kz.signq.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomUserDetailsService {
    UserDetailsService userDetailsService();

    User create(User user) throws EmailAlreadyExistsException;
}
