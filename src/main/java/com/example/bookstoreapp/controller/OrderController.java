package com.example.bookstoreapp.controller;

import com.example.bookstoreapp.dto.orderdto.OrderDto;
import com.example.bookstoreapp.dto.orderdto.OrderRequestDto;
import com.example.bookstoreapp.dto.orderdto.OrderResponseDto;
import com.example.bookstoreapp.dto.orderdto.OrderUpdateDto;
import com.example.bookstoreapp.entity.User;
import com.example.bookstoreapp.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Place an order",
            description = "Allows a user to place an order for books in "
                    + "their shopping cart. Only users with "
                    + "'USER' authority can perform this action."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order placed successfully",
                    content = @Content(schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request data"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - User must be authenticated"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - User does not have permission"),
    })
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public OrderDto save(Authentication authentication,
                         @Valid @RequestBody OrderRequestDto orderRequestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.save(user.getId(), orderRequestDto);
    }

    @Operation(
            summary = "Get order history",
            description = "Retrieves a paginated list of past orders for the authenticated user. "
                    + "Only users with 'USER' authority can access this."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order history retrieved successfully",
                    content = @Content(array = @ArraySchema(schema =
                    @Schema(implementation = OrderResponseDto.class)))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request parameters"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - User must be authenticated"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - User does not have permission"),
    })
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public List<OrderResponseDto> getAllOrderHistory(Authentication authentication,
                                                     Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.getAllOrderHistory(user.getId(), pageable);
    }

    @Operation(
            summary = "Update order status",
            description = "Allows an admin to update the status of an order. "
                    + "Only users with 'ADMIN' authority can perform this action."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                    content = @Content(schema = @Schema(implementation = OrderUpdateDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request data"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - User must be authenticated"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - User does not have ADMIN permissions"),
            @ApiResponse(responseCode = "404",
                    description = "Order not found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("{id}")
    public OrderUpdateDto update(Authentication authentication, @PathVariable Long id,
                                 @Valid @RequestBody OrderUpdateDto updateDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.update(user.getId(), id, updateDto);
    }
}



