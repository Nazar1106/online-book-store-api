package com.example.bookstoreapp;

import com.example.bookstoreapp.dto.userdto.UserRegistrationRequestDto;
import com.example.bookstoreapp.dto.userdto.UserResponseDto;
import com.example.bookstoreapp.entity.Role;
import com.example.bookstoreapp.entity.RoleName;
import com.example.bookstoreapp.entity.User;
import java.util.Set;

public class UserUtil {

    public static Role getRole() {
        Role role = new Role();
        role.setId(1L);
        role.setRole(RoleName.USER);
        return role;
    }

    public static User getUserBeforeSaveIntoDb() {
        User user = new User();
        user.setEmail("testUser@Gmail.com");
        user.setPassword("12345678");
        user.setPassword("12345678");
        user.setFirstName("TestUserName1");
        user.setLastName("TestLastName1");
        user.setShippingAddress("TesShippingAddress 1");
        user.setRoles(Set.of(getRole()));

        return user;
    }

    public static User getUserAfterSaveIntoDb() {
        User user = new User();
        user.setId(1L);
        user.setEmail("testUser@Gmail.com");
        user.setPassword("12345678");
        user.setPassword("12345678");
        user.setFirstName("TestUserName1");
        user.setLastName("TestLastName1");
        user.setShippingAddress("TesShippingAddress 1");
        user.setRoles(Set.of(getRole()));

        return user;
    }

    public static UserRegistrationRequestDto getUserRequestDto() {
        UserRegistrationRequestDto user = new UserRegistrationRequestDto();
        user.setEmail("testUser@Gmail.com");
        user.setPassword("12345678");
        user.setRepeatPassword("12345678");
        user.setFirstName("TestUserName1");
        user.setLastName("TestLastName1");
        user.setShippingAddress("TesShippingAddress 1");
        return user;
    }

    public static UserResponseDto getUserResponseDto() {
        UserResponseDto user = new UserResponseDto();
        user.setId(1L);
        user.setEmail("testUser@Gmail.com");
        user.setFirstName("TestUserName1");
        user.setLastName("TestLastName1");
        user.setShippingAddress("TesShippingAddress 1");
        return user;
    }
}
