package com.kz.signq.service;

import com.kz.signq.dto.AuthenticationDto;
import com.kz.signq.dto.LoginDto;
import com.kz.signq.dto.RegisterDto;
import com.kz.signq.exception.EmailAlreadyExistsException;

public interface AuthenticationService {

    AuthenticationDto register(RegisterDto registerDto) throws EmailAlreadyExistsException;

    AuthenticationDto login(LoginDto loginDto);
}
