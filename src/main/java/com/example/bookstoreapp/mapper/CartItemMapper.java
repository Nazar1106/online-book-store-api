package com.example.bookstoreapp.mapper;

import com.example.bookstoreapp.config.MapperConfig;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartItemDto;
import com.example.bookstoreapp.entity.CartItem;
import com.example.bookstoreapp.entity.ShoppingCart;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(source = "cartItem.book.title", target = "bookTitle")
    ShoppingCartItemDto toItemDto(CartItem cartItem);

    @Mapping(target = "userId", source = "cartItem.shoppingCart.user.id")
    @Mapping(target = "cartItems", source = "cartItem.shoppingCart.cartItems")
    ShoppingCartDto toResponseDto(CartItem cartItem);

    @Mapping(source = "user.id", target = "userId")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Named("mapCartItems")
    default Set<ShoppingCartItemDto> mapCartItems(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toItemDto)
                .collect(Collectors.toSet());
    }
}
