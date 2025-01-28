package com.example.bookstoreapp.controller;

import com.example.bookstoreapp.dto.userdto.UserLoginRequestDto;
import com.example.bookstoreapp.dto.userdto.UserLoginResponseDto;
import com.example.bookstoreapp.dto.userdto.UserRegistrationRequestDto;
import com.example.bookstoreapp.dto.userdto.UserResponseDto;
import com.example.bookstoreapp.exception.RegistrationException;
import com.example.bookstoreapp.security.AuthenticationService;
import com.example.bookstoreapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserService userService;

    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public UserResponseDto register(@Valid @RequestBody UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@Valid @RequestBody UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
