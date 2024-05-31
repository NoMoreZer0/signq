package com.kz.signq.controller;

import com.kz.signq.dto.authentication.AuthenticationDto;
import com.kz.signq.dto.authentication.LoginDto;
import com.kz.signq.dto.authentication.RegisterDto;
import com.kz.signq.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationDto> register(@RequestBody RegisterDto registerDto) {
        var response = authenticationService.register(registerDto);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDto> login(@RequestBody LoginDto loginDto) {
        var response = authenticationService.login(loginDto);
        return ResponseEntity.ok().body(response);
    }
}
