package com.example.bookstoreapp.controller;

import com.example.bookstoreapp.dto.orderitemdto.OrderItemDto;
import com.example.bookstoreapp.entity.User;
import com.example.bookstoreapp.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders/{orderId}/items")
public class OrderItemsController {

    private final OrderItemService orderItemService;

    @Operation(
            summary = "Get all order items by order ID",
            description = "Retrieves a paginated list of items for a specific order. "
                    + "Only users with 'USER' authority can access this."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order items retrieved successfully",
                    content = @Content(array = @ArraySchema(
                            schema = @Schema(implementation = OrderItemDto.class)))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request parameters"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - User must be authenticated"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - User does not have permission"),
            @ApiResponse(responseCode = "404",
                    description = "Order not found"),
    })
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public List<OrderItemDto> getAllByOrderId(Authentication authentication,
                                              @PathVariable Long orderId, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderItemService.getAllByOrderId(user.getId(), orderId, pageable);
    }

    @Operation(
            summary = "Get a specific order item by ID",
            description = "Retrieves details of a specific item within an order. "
                    + "Only users with 'USER' authority can access this."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Order item retrieved successfully",
                    content = @Content(schema = @Schema(implementation = OrderItemDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request parameters"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - User must be authenticated"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - User does not have permission"),
            @ApiResponse(responseCode = "404",
                    description = "Order or OrderItem not found"),
    })
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("{id}")
    public Page<OrderItemDto> getById(Authentication authentication,
                                      @PathVariable Long orderId,
                                      @PathVariable Long id, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderItemService.getBySpecificId(user.getId(), orderId, id, pageable);
    }
}
