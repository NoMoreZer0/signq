package com.kz.signq.service.impl;

import com.kz.signq.db.UserDb;
import com.kz.signq.exception.EmailAlreadyExistsException;
import com.kz.signq.model.User;
import com.kz.signq.service.CustomUserDetailsService;
import com.kz.signq.utils.ErrorCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
    private UserDb db;

    @Autowired
    public CustomUserDetailsServiceImpl(UserDb db) {
        this.db = db;
    }

    public User save(User user) {
        return db.save(user);
    }

    @Override
    public User create(User user) throws EmailAlreadyExistsException {
        var opt = db.findUserByEmail(user.getEmail());
        if (opt.isPresent()) {
            throw new EmailAlreadyExistsException(
                    ErrorCodeUtil.ERR_EMAIL_ALREADY_EXIST.name(),
                    "Пользователь с таким email уже существует"
            );
        }
        return save(user);
    }

    public User getByUsername(String username) {
        return db.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    @Override
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

}
