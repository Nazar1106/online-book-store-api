package com.example.bookstoreapp.mapper;

import com.example.bookstoreapp.config.MapperConfig;
import com.example.bookstoreapp.dto.orderitemdto.OrderItemResponseDto;
import com.example.bookstoreapp.entity.CartItem;
import com.example.bookstoreapp.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "price", source = "book.price")
    OrderItem cartItemToOrderItem(CartItem cartItem);

    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toDto(OrderItem orderItem);
}
