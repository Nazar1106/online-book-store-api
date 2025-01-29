package com.example.bookstoreapp.dto.categorydto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestDto {

    @Column(nullable = false)
    private String name;

    private String description;
}
