package com.example.bookstoreapp.dto.orderdto;

import com.example.bookstoreapp.dto.orderitemdto.OrderItemResponseDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDto {

    private Long id;

    private Long userId;

    private List<OrderItemResponseDto> orderItems;

    private LocalDateTime orderDate;

    private BigDecimal total;

    private String status;
}
