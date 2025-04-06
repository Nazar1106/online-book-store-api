package com.example.bookstoreapp.controller;

import com.example.bookstoreapp.dto.cartitemdto.CartItemRequestDto;
import com.example.bookstoreapp.dto.cartitemdto.CartItemUpdateDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartDto;
import com.example.bookstoreapp.entity.User;
import com.example.bookstoreapp.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart", description = "Provides endpoints for managing shopping carts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Operation(
            summary = "Get shopping cart of the authenticated user",
            description = "Retrieves the shopping cart of the currently authenticated user, "
                    + "using their authentication details to fetch the cart. "
                    + "Only users with 'USER' authority can access this endpoint."
    )
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public ShoppingCartDto getByUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getByUserId(user.getId());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Add item to shopping cart",
            description = "Adds a specified item to the shopping cart of "
                    + "the currently authenticated user. "
                    + "The user is identified via their authentication details. "
                    + "Only users with 'USER' authority can access this endpoint."
    )
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ShoppingCartDto save(Authentication authentication,
                                @RequestBody @Valid CartItemRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.save(user.getId(),requestDto);
    }

    @Operation(
            summary = "Update item in shopping cart",
            description = "Updates the quantity or details of an item in the currently "
                    + "authenticated user's shopping cart. "
                    + "The user is identified via their authentication details. "
                    + "Only users with 'USER' authority can access this endpoint."
    )
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/items/{cartItemId}")
    public ShoppingCartDto update(Authentication authentication,
                                  @Parameter(
                                          description = "ID of the cart item to be updated")
                                  @PathVariable Long cartItemId,
                                  @RequestBody @Valid CartItemUpdateDto updateDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.update(user.getId(), cartItemId, updateDto);
    }

    @Operation(
            summary = "Remove item from shopping cart",
            description = "Deletes an item from the authenticated user's shopping cart. "
                    + "The user is identified through their authentication details. "
                    + "Upon successful removal, the response contains "
                    + "no content (HTTP status 204). "
                    + "Only users with 'USER' authority can access this endpoint."
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/items/{cartItemId}")
    public void deleteById(Authentication authentication, @PathVariable Long cartItemId) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.deleteById(user.getId(), cartItemId);
    }
}
