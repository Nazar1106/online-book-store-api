package com.example.bookstoreapp.dto.orderdto;

import com.example.bookstoreapp.dto.orderitemdto.OrderItemSaveDto;
import com.example.bookstoreapp.entity.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {

    @NotNull
    private Long userId;

    @NotNull
    private Status status;

    @NotNull
    private BigDecimal total;

    @NotNull
    private LocalDateTime orderDate;

    @NotBlank
    private String shippingAddress;

    private List<OrderItemSaveDto> orderItems;
}
