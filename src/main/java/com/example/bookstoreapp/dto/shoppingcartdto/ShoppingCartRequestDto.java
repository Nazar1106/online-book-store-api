package com.example.bookstoreapp.dto.shoppingcartdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingCartRequestDto {

    private Long userId;
    private Long bookId;
    private Integer quantity;
}
