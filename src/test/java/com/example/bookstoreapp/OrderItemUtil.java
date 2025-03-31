package com.example.bookstoreapp;

import com.example.bookstoreapp.dto.orderitemdto.OrderItemResponseDto;
import com.example.bookstoreapp.entity.OrderItem;
import java.math.BigDecimal;

public class OrderItemUtil {

    public static OrderItem getOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setQuantity(20);
        orderItem.setBook(BookUtil.getBook());
        orderItem.setPrice(BigDecimal.valueOf(40));

        return orderItem;
    }

    public static OrderItemResponseDto getOrderItemResponseDto() {
        OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();
        orderItemResponseDto.setId(1L);
        orderItemResponseDto.setQuantity(20);
        orderItemResponseDto.setBookId(1L);

        return orderItemResponseDto;
    }

}
