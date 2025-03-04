package com.example.bookstoreapp.dto.orderitemdto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    @NotNull
    private Long orderId;

    @NotNull
    private Long bookId;

    @Positive
    private int quantity;

    @NotNull
    private BigDecimal price;
}
