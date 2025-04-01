package com.example.bookstoreapp.testutil;

import com.example.bookstoreapp.dto.userdto.UserLoginRequestDto;
import com.example.bookstoreapp.dto.userdto.UserRegistrationRequestDto;
import com.example.bookstoreapp.dto.userdto.UserResponseDto;
import com.example.bookstoreapp.entity.Role;
import com.example.bookstoreapp.entity.RoleName;
import com.example.bookstoreapp.entity.User;
import java.util.Set;

public class UserUtil {

    public static Role getRoleUser() {
        Role role = new Role();
        role.setId(1L);
        role.setRole(RoleName.USER);
        return role;
    }

    public static Role getRoleAdmin() {
        Role role = new Role();
        role.setId(1L);
        role.setRole(RoleName.ADMIN);
        return role;
    }

    public static User getUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john.doe@example.com");
        user.setPassword("12345678");
        user.setPassword("12345678");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setShippingAddress("123 Main St, Springfield, IL");
        user.setRoles(Set.of(getRoleUser()));

        return user;
    }

    public static User getSecondUser() {
        User user = new User();
        user.setId(2L);
        user.setEmail("jane.smith@example.com");
        user.setPassword("12345678");
        user.setPassword("12345678");
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setShippingAddress("456 Oak St, Lincoln, NE");
        user.setRoles(Set.of(getRoleUser()));

        return user;
    }

    public static User getAdmin() {
        User user = new User();
        user.setId(1L);
        user.setEmail("alice.jones@example.com");
        user.setPassword("12345678");
        user.setPassword("12345678");
        user.setFirstName("Alice");
        user.setLastName("Jones");
        user.setShippingAddress("789 Pine St, Omaha, NE");
        user.setRoles(Set.of(getRoleAdmin()));

        return user;
    }

    public static User getUserBeforeSaveIntoDb() {
        User user = new User();
        user.setEmail("testUser@Gmail.com");
        user.setPassword("12345678");
        user.setPassword("12345678");
        user.setFirstName("TestUserName1");
        user.setLastName("TestLastName1");
        user.setShippingAddress("TesShippingAddress 1");
        user.setRoles(Set.of(getRoleUser()));

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
        user.setRoles(Set.of(getRoleUser()));

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

    public static UserLoginRequestDto getUserLoginRequestDto() {
        return new UserLoginRequestDto("john.doe@example.com", "12345678");
    }
}
