package com.example.bookstoreapp.dto.cartitemdto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItemResponseUpdateDto {

    @NotNull
    private Integer quantity;
}
