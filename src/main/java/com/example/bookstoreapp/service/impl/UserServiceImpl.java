package com.example.bookstoreapp.service.impl;

import com.example.bookstoreapp.dto.userdto.UserRegistrationRequestDto;
import com.example.bookstoreapp.dto.userdto.UserResponseDto;
import com.example.bookstoreapp.entity.Role;
import com.example.bookstoreapp.entity.RoleName;
import com.example.bookstoreapp.entity.User;
import com.example.bookstoreapp.exception.RegistrationException;
import com.example.bookstoreapp.mapper.UserMapper;
import com.example.bookstoreapp.repository.role.RoleRepository;
import com.example.bookstoreapp.repository.user.UserRepository;
import com.example.bookstoreapp.service.ShoppingCartService;
import com.example.bookstoreapp.service.UserService;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {

    public static final String CAN_T_REGISTER_USER_MSG = "Can't register user ";
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartService shoppingCartService;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto registerUser(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(CAN_T_REGISTER_USER_MSG);
        }
        Role role = roleRepository.byRoleName(RoleName.USER);
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRoles(Set.of(role));
        userRepository.save(user);
        shoppingCartService.saveShoppingCartForUser(user);
        return userMapper.toDto(user);
    }
}
