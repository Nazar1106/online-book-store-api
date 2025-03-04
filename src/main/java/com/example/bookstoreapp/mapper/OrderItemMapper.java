package com.example.bookstoreapp.mapper;

import com.example.bookstoreapp.config.MapperConfig;
import com.example.bookstoreapp.dto.orderitemdto.OrderItemDto;
import com.example.bookstoreapp.dto.orderitemdto.OrderItemResponseDto;
import com.example.bookstoreapp.entity.CartItem;
import com.example.bookstoreapp.entity.OrderItem;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {

    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toDto(OrderItem orderItem);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "price", source = "book.price")
    OrderItemDto toDto(CartItem cartItem);

    Set<OrderItemDto> toDtos(List<CartItem> cartItems);

    @Mapping(source = "orderId", target = "order.id")
    @Mapping(source = "bookId", target = "book.id")
    OrderItem toModel(OrderItemDto orderItemDto);

    Set<OrderItem> toModels(Set<OrderItemDto> set);
}
