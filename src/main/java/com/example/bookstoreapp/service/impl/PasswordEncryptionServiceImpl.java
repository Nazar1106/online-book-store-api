package com.example.bookstoreapp.service.impl;

import com.example.bookstoreapp.repository.user.UserRepository;
import com.example.bookstoreapp.service.PasswordEncryptionService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Transactional
@Component
public class PasswordEncryptionServiceImpl implements PasswordEncryptionService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void hashPasswords() {
        userRepository.findAll().forEach(user -> {
            if (!user.getPassword().startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            }
        });
    }
}
