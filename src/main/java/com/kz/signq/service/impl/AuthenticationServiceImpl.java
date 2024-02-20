package com.kz.signq.service.impl;

import com.kz.signq.dto.AuthenticationDto;
import com.kz.signq.dto.LoginDto;
import com.kz.signq.dto.RegisterDto;
import com.kz.signq.exception.EmailAlreadyExistsException;
import com.kz.signq.model.Role;
import com.kz.signq.model.User;
import com.kz.signq.service.AuthenticationService;
import com.kz.signq.service.CustomUserDetailsService;
import com.kz.signq.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtill;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationServiceImpl(
            CustomUserDetailsService customUserDetailsService,
            JwtUtil jwtUtill,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtill = jwtUtill;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthenticationDto register(RegisterDto registerDto) throws EmailAlreadyExistsException {
        var user = User.builder()
                .name(registerDto.getName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        customUserDetailsService.create(user);

        var token = jwtUtill.generateToken(user);

        return AuthenticationDto.builder()
                .token(token)
                .build();
    }

    @Override
    public AuthenticationDto login(LoginDto loginDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword()
        ));

        var user = customUserDetailsService
                .userDetailsService()
                .loadUserByUsername(loginDto.getEmail());

        var token = jwtUtill.generateToken(user);

        return AuthenticationDto.builder()
                .token(token)
                .build();
    }
}
