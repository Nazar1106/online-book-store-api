package com.example.bookstoreapp.dto.cartitemdto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemUpdateDto {

    @Positive
    private int quantity;
}
