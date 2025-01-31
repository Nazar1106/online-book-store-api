package com.example.bookstoreapp.dto.shoppingcartdto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingCartDto {

    private Long id;

    private Long userId;

    private Set<ShoppingCartItemDto> cartItems;
}
