package com.kz.signq.service;

import com.kz.signq.dto.authentication.AuthenticationDto;
import com.kz.signq.dto.authentication.LoginDto;
import com.kz.signq.dto.authentication.RegisterDto;
import com.kz.signq.exception.EmailAlreadyExistsException;

public interface AuthenticationService {

    AuthenticationDto register(RegisterDto registerDto) throws EmailAlreadyExistsException;

    AuthenticationDto login(LoginDto loginDto);
}
