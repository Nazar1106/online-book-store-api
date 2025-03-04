package com.example.bookstoreapp.service;

import com.example.bookstoreapp.dto.orderdto.OrderRequestDto;
import com.example.bookstoreapp.dto.orderdto.OrderResponseDto;
import com.example.bookstoreapp.dto.orderdto.OrderUpdateDto;
import com.example.bookstoreapp.dto.orderitemdto.OrderItemResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponseDto save(Long authentication, OrderRequestDto requestDto);

    List<OrderResponseDto> getAllOrderHistory(Long authentication, Pageable pageable);

    OrderUpdateDto update(Long authentication, Long orderId, OrderUpdateDto updateStatus);

    Page<OrderItemResponseDto> getAllOrderItemsByOrderId(Long authentication, Long orderId,
                                                         Pageable pageable);

    OrderItemResponseDto getBySpecificId(Long authentication, Long orderId, Long itemId);
}
