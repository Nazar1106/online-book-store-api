package com.example.bookstoreapp.service;

import com.example.bookstoreapp.dto.userdto.UserRegistrationRequestDto;
import com.example.bookstoreapp.dto.userdto.UserResponseDto;
import com.example.bookstoreapp.exception.RegistrationException;

public interface UserService {

    UserResponseDto registerUser(UserRegistrationRequestDto requestDto)
            throws RegistrationException;

}
