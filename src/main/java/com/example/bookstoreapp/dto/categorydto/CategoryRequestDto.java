package com.example.bookstoreapp.dto.categorydto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestDto {

    @NotBlank
    private String name;

    private String description;
}
