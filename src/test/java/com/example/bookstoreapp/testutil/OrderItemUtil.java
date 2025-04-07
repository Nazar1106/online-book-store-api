package com.example.bookstoreapp.testutil;

import com.example.bookstoreapp.dto.orderitemdto.OrderItemResponseDto;
import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.entity.Order;
import com.example.bookstoreapp.entity.OrderItem;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class OrderItemUtil {

    public static OrderItem getOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setQuantity(20);
        orderItem.setBook(BookUtil.getBook());
        orderItem.setPrice(BigDecimal.valueOf(40));

        return orderItem;
    }

    public static Optional<OrderItem> getOptionalOrderItem() {
        OrderItem orderItem = new OrderItem();
        Order order = new Order();
        orderItem.setId(1L);
        order.setId(1L);
        orderItem.setQuantity(20);
        orderItem.setPrice(BigDecimal.valueOf(150).setScale(2, RoundingMode.HALF_UP));
        orderItem.setOrder(order);
        Book book = new Book();
        orderItem.setBook(book);

        return Optional.of(orderItem);
    }

    public static Page<OrderItem> getPageOrderItem() {
        OrderItem orderItem = new OrderItem();
        Order order = new Order();
        orderItem.setId(1L);
        order.setId(1L);
        orderItem.setQuantity(20);
        orderItem.setPrice(BigDecimal.valueOf(150).setScale(2, RoundingMode.HALF_UP));
        orderItem.setOrder(order);

        List<OrderItem> orderItemList = List.of(orderItem);

        return new PageImpl<>(orderItemList, PageRequest.of(0, 1), orderItemList.size());
    }

    public static Page<OrderItem> getPageOrderItems() {
        OrderItem orderItem = new OrderItem();
        Order order = new Order();
        orderItem.setId(2L);
        order.setId(2L);
        orderItem.setQuantity(30);
        orderItem.setPrice(BigDecimal.valueOf(250).setScale(2, RoundingMode.HALF_UP));
        orderItem.setOrder(order);

        OrderItem orderItem2 = new OrderItem();
        Order order2 = new Order();
        orderItem2.setId(3L);
        order2.setId(2L);
        orderItem2.setQuantity(400);
        orderItem2.setPrice(BigDecimal.valueOf(250).setScale(2, RoundingMode.HALF_UP));
        orderItem2.setOrder(order2);

        List<OrderItem> orderItemList = List.of(orderItem, orderItem2);

        return new PageImpl<>(orderItemList, PageRequest.of(0, 2), orderItemList.size());
    }

    public static OrderItemResponseDto getOrderItemResponseDto() {
        OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();
        orderItemResponseDto.setId(1L);
        orderItemResponseDto.setQuantity(20);
        orderItemResponseDto.setBookId(1L);

        return orderItemResponseDto;
    }
}
