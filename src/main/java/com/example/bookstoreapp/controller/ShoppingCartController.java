package com.example.bookstoreapp.controller;

import com.example.bookstoreapp.dto.cartitemdto.CartItemResponseUpdateDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartRequestDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartResponseDto;
import com.example.bookstoreapp.service.ShoppingCartService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public List<ShoppingCartDto> getAll() {
        return shoppingCartService.getAll();
    }

    @PostMapping
    public ShoppingCartResponseDto save(@RequestBody @Valid ShoppingCartRequestDto requestDto) {
        return shoppingCartService.save(requestDto);
    }

    @PutMapping("/items/{cartItemId}")
    public CartItemResponseUpdateDto update(@PathVariable Long cartItemId,
                                            @RequestBody CartItemResponseUpdateDto requestDto) {
        return shoppingCartService.update(cartItemId, requestDto);
    }

    @DeleteMapping("/items/{cartItemId}")
    public void deleteById(@PathVariable Long cartItemId) {
        shoppingCartService.deleteById(cartItemId);
    }
}
