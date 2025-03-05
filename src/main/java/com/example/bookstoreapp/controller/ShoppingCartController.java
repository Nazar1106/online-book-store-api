package com.example.bookstoreapp.controller;

import com.example.bookstoreapp.dto.cartitemdto.CartItemRequestDto;
import com.example.bookstoreapp.dto.cartitemdto.CartItemUpdateDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartDto;
import com.example.bookstoreapp.entity.User;
import com.example.bookstoreapp.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    @Operation(
            summary = "Get shopping cart of the authenticated user",
            description = "Retrieves the shopping cart for the currently "
                    + "authenticated user based on their authentication details.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Shopping cart retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ShoppingCartDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - User must be authenticated"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - User lacks required permissions"),
            @ApiResponse(responseCode = "404",
                    description = "Shopping cart not found for the user"),
    })
    public ShoppingCartDto getByUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getByUserId(user.getId());
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    @Operation(
            summary = "Add item to shopping cart",
            description = "Adds an item to the shopping cart of the authenticated user. "
                    + "The user is identified through authentication.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Item successfully added to the shopping cart",
                    content =
                    @Content(schema = @Schema(implementation = ShoppingCartDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request data"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - User must be authenticated"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - User lacks required permissions"),
    })
    public ShoppingCartDto save(Authentication authentication,
                                        @RequestBody @Valid CartItemRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.save(user.getId(),requestDto);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/items/{cartItemId}")
    @Operation(
            summary = "Update item in shopping cart",
            description = "Updates the quantity or details of an item in the "
                    + "authenticated user's shopping cart."
                    + "The user is identified through authentication.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Item updated successfully",
                            content = @Content(schema =
                            @Schema(implementation = ShoppingCartDto.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid input data"),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized - User must be authenticated"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden - User lacks required permissions"),
                    @ApiResponse(responseCode = "404",
                            description = "Item not found"),
            }
    )
    public ShoppingCartDto update(Authentication authentication,
                                          @Parameter(
                                                  description = "ID of the cart item to be updated")
                                          @PathVariable Long cartItemId,
                                          @RequestBody @Valid CartItemUpdateDto updateDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.update(user.getId(), cartItemId, updateDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/items/{cartItemId}")
    @Operation(
            summary = "Remove item from shopping cart",
            description = "Deletes an item from the authenticated user's shopping cart. "
                    + "The user is identified through authentication. "
                    + "Returns no content on success.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",
                    description = "Item successfully removed from the shopping cart"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - User must be authenticated"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - User lacks required permissions"),
            @ApiResponse(responseCode = "404",
                    description = "Item not found"),
    })
    public void deleteById(Authentication authentication, @PathVariable Long cartItemId) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.deleteById(user.getId(), cartItemId);
    }
}
