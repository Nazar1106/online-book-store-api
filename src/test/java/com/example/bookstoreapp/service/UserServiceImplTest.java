package com.example.bookstoreapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookstoreapp.UserUtil;
import com.example.bookstoreapp.dto.userdto.UserRegistrationRequestDto;
import com.example.bookstoreapp.dto.userdto.UserResponseDto;
import com.example.bookstoreapp.entity.Role;
import com.example.bookstoreapp.entity.RoleName;
import com.example.bookstoreapp.entity.User;
import com.example.bookstoreapp.exception.RegistrationException;
import com.example.bookstoreapp.mapper.UserMapper;
import com.example.bookstoreapp.repository.role.RoleRepository;
import com.example.bookstoreapp.repository.user.UserRepository;
import com.example.bookstoreapp.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User userBeforeSaveIntoBd;

    private User userAfterSaveIntoBd;

    private UserRegistrationRequestDto requestDto;

    private UserResponseDto userResponseDto;

    private Role role;

    @BeforeEach
    void setUp() {
        userBeforeSaveIntoBd = UserUtil.getUserBeforeSaveIntoDb();
        userAfterSaveIntoBd = UserUtil.getUserAfterSaveIntoDb();
        role = UserUtil.getRole();
        requestDto = UserUtil.getUserRequestDto();
        userResponseDto = UserUtil.getUserResponseDto();
    }

    @Test
    void registerUser_ShouldReturnUserResponseDto_WhenValidUser() throws RegistrationException {
        when(roleRepository.byRoleName(RoleName.USER)).thenReturn(role);
        when(userMapper.toDto(userBeforeSaveIntoBd)).thenReturn(userResponseDto);
        when(userMapper.toModel(requestDto)).thenReturn(userBeforeSaveIntoBd);
        when(passwordEncoder.encode("12345678"))
                .thenReturn("$2a$10$MciJPpHrd8psQfrFmYyBc.j2PGf5k9xP/sOrNuT9pUwac6ah9vzw6");

        when(userRepository.save(userMapper.toModel(requestDto))).thenReturn(userAfterSaveIntoBd);
        when(userRepository.existsByEmail(userBeforeSaveIntoBd.getEmail())).thenReturn(false);

        UserResponseDto responseDto = userService.registerUser(requestDto);

        assertEquals(userResponseDto, responseDto);
        Assertions.assertNull(userBeforeSaveIntoBd.getId());
        Assertions.assertNotNull(userBeforeSaveIntoBd);

        verify(userRepository).save(userBeforeSaveIntoBd);
        verify(passwordEncoder).encode("12345678");
        verify(shoppingCartService).saveShoppingCartForUser(userMapper.toModel(requestDto));
    }

    @Test
    void registerUser_ShouldReturnRegistrationException_WhenUserEmailAlreadyExist() {
        String expectedMessage = "User email already exist";

        when(userRepository.existsByEmail(userBeforeSaveIntoBd.getEmail())).thenReturn(true);

        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> userService.registerUser(requestDto));

        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }
}
