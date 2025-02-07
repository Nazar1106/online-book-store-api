package com.example.bookstoreapp.dto.cartitemdto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItemRequestDto {

    @NotNull
    private Long bookId;
    @NotNull
    @Positive
    private int quantity;
}
