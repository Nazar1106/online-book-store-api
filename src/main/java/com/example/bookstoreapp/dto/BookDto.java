package com.example.bookstoreapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Getter
@Setter
@RestControllerAdvice
public class BookDto {

    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    private String isbn;
    @NotNull
    @Min(0)
    private BigDecimal price;
    @NotBlank
    private String description;
    @NotBlank
    private String coverImage;
}
