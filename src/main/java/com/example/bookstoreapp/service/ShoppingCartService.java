package com.example.bookstoreapp.service;

import com.example.bookstoreapp.dto.cartitemdto.CartItemResponseUpdateDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartRequestDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartResponseDto;
import java.util.List;

public interface ShoppingCartService {

    List<ShoppingCartDto> getAll();

    ShoppingCartResponseDto save(ShoppingCartRequestDto requestDto);

    CartItemResponseUpdateDto update(Long cartItemId, CartItemResponseUpdateDto requestDto);

    void deleteById(Long id);
}
