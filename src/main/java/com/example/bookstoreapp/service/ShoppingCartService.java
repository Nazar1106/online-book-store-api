package com.example.bookstoreapp.service;

import com.example.bookstoreapp.dto.cartitemdto.CartItemRequestDto;
import com.example.bookstoreapp.dto.cartitemdto.CartItemUpdateDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartDto;
import com.example.bookstoreapp.entity.User;

public interface ShoppingCartService {

    ShoppingCartDto getByUserId(Long authenticationId);

    ShoppingCartDto save(Long authenticationId, CartItemRequestDto requestDto);

    void saveShoppingCartForUser(User user);

    ShoppingCartDto update(Long authenticationId, Long cartItemId,
                                   CartItemUpdateDto updateDto);

    void deleteById(Long authenticationId, Long id);
}
