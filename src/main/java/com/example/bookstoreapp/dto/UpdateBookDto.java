package com.example.bookstoreapp.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBookDto {

    @NotNull
    private String title;
    @NotNull
    private String author;
    @NotNull
    private String isbn;
    @NotNull
    private BigDecimal price;
    @NotNull
    private String description;
    @NotNull
    private String coverImage;
}
