package com.example.bookstoreapp.security;

import com.example.bookstoreapp.exception.EntityNotFoundException;
import com.example.bookstoreapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final String CAN_T_FIND_USER_BY_EMAIL = "Can't find user by email ";
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(CAN_T_FIND_USER_BY_EMAIL + email));
    }
}
