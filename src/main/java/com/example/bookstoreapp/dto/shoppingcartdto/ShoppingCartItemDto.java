package com.example.bookstoreapp.dto.shoppingcartdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingCartItemDto {

    private Long id;
    private Long bookId;
    private String bookTitle;
    private int quantity;
}
