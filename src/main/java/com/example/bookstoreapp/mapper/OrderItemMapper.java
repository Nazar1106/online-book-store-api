package com.example.bookstoreapp.mapper;

import com.example.bookstoreapp.config.MapperConfig;
import com.example.bookstoreapp.dto.orderitemdto.OrderItemDto;
import com.example.bookstoreapp.dto.orderitemdto.OrderItemSaveDto;
import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.entity.CartItem;
import com.example.bookstoreapp.entity.Order;
import com.example.bookstoreapp.entity.OrderItem;
import java.util.Collections;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {

    @Mapping(source = "cartItem.book.id", target = "bookId")
    @Mapping(source = "cartItem.quantity", target = "quantity")
    @Mapping(source = "cartItem.book.price", target = "price")
    @Mapping(target = "orderId", ignore = true)
    OrderItemSaveDto toOrderItemDto(CartItem cartItem);

    default OrderItemSaveDto toOrderItemDto(CartItem cartItem, Order order) {
        OrderItemSaveDto dto = toOrderItemDto(cartItem);
        dto.setOrderId(order.getId());
        return dto;
    }

    @Mapping(source = "orderId", target = "order")
    @Mapping(source = "bookId", target = "book")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "price", target = "price")
    OrderItem toOrderItem(OrderItemSaveDto orderItemDto);

    @Mapping(source = "book", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);

    List<OrderItemDto> toDtos(List<OrderItem> orderItem);

    default List<OrderItem> toOrderItems(List<OrderItemSaveDto> orderItemDtos, Order order) {
        if (orderItemDtos == null) {
            return Collections.emptyList();
        }
        return orderItemDtos.stream()
                .map(dto -> {
                    OrderItem orderItem = toOrderItem(dto);
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .toList();
    }

    default Long map(Book book) {
        return book != null ? book.getId() : null;
    }

    default Order mapOrder(Long orderId) {
        if (orderId == null) {
            return null;
        }
        Order order = new Order();
        order.setId(orderId);
        return order;
    }

    default Book mapBook(Long bookId) {
        if (bookId == null) {
            return null;
        }
        Book book = new Book();
        book.setId(bookId);
        return book;
    }
}
