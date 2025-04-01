package com.example.bookstoreapp.testutil;

import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartItemDto;
import com.example.bookstoreapp.entity.CartItem;
import com.example.bookstoreapp.entity.ShoppingCart;
import java.util.Set;

public class ShoppingCartUtil {

    public static ShoppingCart getShoppingCartWithItems() {
        final ShoppingCart shoppingCart = new ShoppingCart();
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setBook(BookUtil.getBook());
        cartItem.setQuantity(15);
        shoppingCart.setId(1L);
        shoppingCart.setUser(UserUtil.getUserAfterSaveIntoDb());
        shoppingCart.setCartItems(Set.of(cartItem));
        shoppingCart.setDeleted(false);
        cartItem.setShoppingCart(shoppingCart);

        return shoppingCart;
    }

    public static ShoppingCart getShoppingCart() {
        final ShoppingCart shoppingCart = new ShoppingCart();
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setBook(BookUtil.getBook());
        cartItem.setQuantity(15);
        shoppingCart.setId(1L);
        shoppingCart.setUser(UserUtil.getUserAfterSaveIntoDb());
        shoppingCart.setCartItems(Set.of(cartItem));
        shoppingCart.setDeleted(false);
        return shoppingCart;
    }

    public static ShoppingCart getShoppingCartWithoutItems() {
        final ShoppingCart shoppingCart = new ShoppingCart();

        shoppingCart.setId(1L);
        shoppingCart.setUser(UserUtil.getUserAfterSaveIntoDb());
        shoppingCart.setCartItems(Set.of());
        shoppingCart.setDeleted(false);
        shoppingCart.setCartItems(Set.of());

        return shoppingCart;
    }

    public static ShoppingCartDto getShoppingCartDto() {
        final ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        ShoppingCartItemDto shoppingCartItemDto = new ShoppingCartItemDto();

        shoppingCartItemDto.setId(1L);
        shoppingCartItemDto.setBookId(1L);
        shoppingCartItemDto.setBookTitle("Test title");
        shoppingCartItemDto.setQuantity(30);

        shoppingCartDto.setId(1L);
        shoppingCartDto.setUserId(1L);
        shoppingCartDto.setCartItems(Set.of(shoppingCartItemDto));

        return shoppingCartDto;
    }

    public static Integer getExpectedQuantity() {
        return getShoppingCartDto().getCartItems()
                .stream().filter((e) -> e.getId() == 1L)
                .map(ShoppingCartItemDto::getQuantity)
                .findFirst()
                .orElse(null);
    }
}
