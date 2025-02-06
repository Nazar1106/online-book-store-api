package com.example.bookstoreapp.service;

import com.example.bookstoreapp.dto.cartitemdto.CartItemRequestDto;
import com.example.bookstoreapp.dto.cartitemdto.CartItemUpdateDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartResponseDto;

public interface ShoppingCartService {

    ShoppingCartDto getByUserId(Long authenticationId);

    ShoppingCartResponseDto save(Long authenticationId, CartItemRequestDto requestDto);

    ShoppingCartResponseDto update(Long authenticationId, Long cartItemId,
                                   CartItemUpdateDto updateDto);

    void deleteById(Long authenticationId, Long id);
}
