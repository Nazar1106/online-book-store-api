package com.example.bookstoreapp.mapper;

import com.example.bookstoreapp.config.MapperConfig;
import com.example.bookstoreapp.dto.cartitemdto.CartItemResponseUpdateDto;
import com.example.bookstoreapp.entity.CartItem;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {

    CartItemResponseUpdateDto toDto(CartItem cartItem);

}
