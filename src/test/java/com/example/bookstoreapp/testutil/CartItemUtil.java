package com.example.bookstoreapp.testutil;

import static com.example.bookstoreapp.testutil.ShoppingCartUtil.getShoppingCartWithItems;

import com.example.bookstoreapp.dto.cartitemdto.CartItemRequestDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartItemDto;
import com.example.bookstoreapp.entity.CartItem;

public class CartItemUtil {

    public static CartItemRequestDto getCartItemRequestDto() {
        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setQuantity(15);
        cartItemRequestDto.setBookId(1L);

        return cartItemRequestDto;
    }

    public static CartItem getCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setBook(BookUtil.getBook());
        cartItem.setShoppingCart(getShoppingCartWithItems());
        cartItem.setQuantity(30);

        return cartItem;
    }

    public static ShoppingCartItemDto getShoppingCartItem() {
        ShoppingCartItemDto shoppingCartItemDto = new ShoppingCartItemDto();
        shoppingCartItemDto.setId(1L);
        shoppingCartItemDto.setBookId(1L);
        shoppingCartItemDto.setBookTitle("NewBookTitle1");
        shoppingCartItemDto.setQuantity(35);

        return shoppingCartItemDto;
    }

    public static ShoppingCartItemDto getShoppingCartItemDto() {
        ShoppingCartItemDto shoppingCartItemDto = new ShoppingCartItemDto();
        shoppingCartItemDto.setId(1L);
        shoppingCartItemDto.setBookId(1L);
        shoppingCartItemDto.setBookTitle("NewBookTitle1");
        shoppingCartItemDto.setQuantity(20);
        return shoppingCartItemDto;
    }
}
