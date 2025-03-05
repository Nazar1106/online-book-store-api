package com.example.bookstoreapp.service;

import com.example.bookstoreapp.dto.orderdto.OrderRequestDto;
import com.example.bookstoreapp.dto.orderdto.OrderResponseDto;
import com.example.bookstoreapp.dto.orderdto.OrderUpdateDto;
import com.example.bookstoreapp.dto.orderitemdto.OrderItemResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponseDto createOrder(Long userId, OrderRequestDto orderDto);

    List<OrderResponseDto> getUserOrderHistory(Long authentication, Pageable pageable);

    OrderUpdateDto updateOrderStatus(Long authentication, Long orderId,
                                     OrderUpdateDto updateStatus);

    Page<OrderItemResponseDto> getOrderItems(Long authentication, Long orderId,
                                                         Pageable pageable);

    OrderItemResponseDto getOrderItem(Long authentication, Long orderId, Long itemId);
}
