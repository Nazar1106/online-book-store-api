package com.example.bookstoreapp.testutil;

import com.example.bookstoreapp.dto.orderdto.OrderRequestDto;
import com.example.bookstoreapp.dto.orderdto.OrderResponseDto;
import com.example.bookstoreapp.dto.orderdto.OrderUpdateDto;
import com.example.bookstoreapp.dto.orderitemdto.OrderItemResponseDto;
import com.example.bookstoreapp.entity.Order;
import com.example.bookstoreapp.entity.Status;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OrderUtil {

    public static OrderResponseDto getOrderResponseDto() {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(1L);
        orderResponseDto.setUserId(1L);
        orderResponseDto.setOrderDate(LocalDateTime.now());
        orderResponseDto.setOrderItems(List.of(getOrderItemResponseDto()));
        orderResponseDto.setStatus("PENDING");
        orderResponseDto.setTotal(BigDecimal.valueOf(40));

        return orderResponseDto;
    }

    public static OrderItemResponseDto getOrderItemResponseDto() {
        OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();
        orderItemResponseDto.setId(1L);
        orderItemResponseDto.setQuantity(20);
        orderItemResponseDto.setBookId(1L);

        return orderItemResponseDto;

    }

    public static OrderRequestDto getOrderRequestDto() {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setShippingAddress("123 Main St, Springfield, IL");

        return orderRequestDto;
    }

    public static OrderUpdateDto getOrderUpdateDto() {
        OrderUpdateDto orderUpdateDto = new OrderUpdateDto();
        orderUpdateDto.setStatus(Status.DELIVERED);

        return orderUpdateDto;
    }

    public static List<Order> getListOrders() {
        Order order = new Order();
        order.setId(1L);
        order.setTotal(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP));
        order.setStatus(Status.PENDING);
        order.setShippingAddress("testShippingAddress");
        order.setDeleted(false);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setTotal(BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_UP));
        order2.setStatus(Status.COMPLETED);
        order2.setShippingAddress("TEST ADDRESS 1");
        order2.setDeleted(false);

        List<Order> list = new ArrayList<>();
        list.add(order);
        list.add(order2);

        return list;
    }

    public static List<Order> getListOrder() {
        Order order = new Order();
        order.setId(3L);
        order.setTotal(BigDecimal.valueOf(350).setScale(2, RoundingMode.HALF_UP));
        order.setStatus(Status.DELIVERED);
        order.setShippingAddress("TEST ADDRESS 3");
        order.setDeleted(false);

        List<Order> list = new ArrayList<>();
        list.add(order);

        return list;
    }

    public static Order getOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setUser(UserUtil.getUserAfterSaveIntoDb());
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(BigDecimal.valueOf(40));
        order.setStatus(Status.PENDING);
        order.setOrderItems(Set.of(OrderItemUtil.getOrderItem()));
        order.setDeleted(false);
        order.setShippingAddress("Test shipping address");

        return order;
    }

    public static OrderResponseDto getNewOrderResponseDto() {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(4L);
        orderResponseDto.setUserId(1L);
        OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();
        orderItemResponseDto.setId(4L);
        orderItemResponseDto.setBookId(1L);
        orderItemResponseDto.setQuantity(20);
        orderResponseDto.setOrderItems(List.of(orderItemResponseDto));
        orderResponseDto.setTotal(BigDecimal.valueOf(3000));
        orderResponseDto.setStatus("PENDING");

        return orderResponseDto;
    }

    public static List<OrderResponseDto> getListResponseDto() {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(1L);
        orderResponseDto.setUserId(1L);
        OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();
        orderItemResponseDto.setId(1L);
        orderItemResponseDto.setBookId(1L);
        orderItemResponseDto.setQuantity(20);
        orderResponseDto.setOrderItems(List.of(orderItemResponseDto));
        orderResponseDto.setTotal(BigDecimal.valueOf(500)
                .setScale(2, RoundingMode.HALF_UP));
        orderResponseDto.setStatus("PENDING");

        return List.of(orderResponseDto);
    }

}
