package com.example.bookstoreapp.service;

import com.example.bookstoreapp.dto.orderdto.OrderDto;
import com.example.bookstoreapp.dto.orderdto.OrderRequestDto;
import com.example.bookstoreapp.dto.orderdto.OrderResponseDto;
import com.example.bookstoreapp.dto.orderdto.OrderUpdateDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderDto save(Long authentication, OrderRequestDto requestDto);

    List<OrderResponseDto> getAllOrderHistory(Long authentication, Pageable pageable);

    OrderUpdateDto update(Long authentication, Long orderId, OrderUpdateDto updateStatus);
}
