package com.example.bookstoreapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBookDto {

    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    private String isbn;
    @Positive
    @NotNull
    private BigDecimal price;
    @NotBlank
    private String description;
    @NotBlank
    private String coverImage;
}
