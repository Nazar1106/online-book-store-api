package com.example.bookstoreapp.dto.userdto;

import com.example.bookstoreapp.validation.FieldMatch;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@FieldMatch(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords do not match!"
)
@Getter
@Setter
public class UserRegistrationRequestDto {

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;
    @Length(min = 8, max = 20)
    @NotBlank
    private String password;
    @Length(min = 8, max = 20)
    @NotBlank
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    private String shippingAddress;
}
