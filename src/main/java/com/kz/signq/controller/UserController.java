package com.kz.signq.controller;

import com.kz.signq.dto.user.UserDto;
import com.kz.signq.exception.ErrorCodeException;
import com.kz.signq.model.User;
import com.kz.signq.service.UserService;
import com.kz.signq.utils.ErrorCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/data")
    public ResponseEntity<?> getUserData() {
        var user = getCurrentUser();
        return ResponseEntity.ok().body(userService.getUserData(user));
    }

    @PostMapping("/data")
    public ResponseEntity<?> editUserData(@RequestBody UserDto userDto) {
        var user = getCurrentUser();
        try {
            return ResponseEntity.ok().body(userService.editUserData(userDto, user));
        } catch (ErrorCodeException e) {
            return ResponseEntity.badRequest().body(ErrorCodeUtil.toExceptionDto(e));
        }
    }

    private User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }
}
