package com.example.bookstoreapp.service;

import com.example.bookstoreapp.dto.orderitemdto.OrderItemDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderItemService {

    List<OrderItemDto> getAllByOrderId(Long authentication, Long orderId, Pageable pageable);

    Page<OrderItemDto> getBySpecificId(Long authentication, Long orderId, Long itemId,
                                      Pageable pageable);
}
