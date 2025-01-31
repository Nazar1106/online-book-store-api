package com.example.bookstoreapp.mapper;

import com.example.bookstoreapp.config.MapperConfig;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartItemDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartRequestDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartResponseDto;
import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.entity.CartItem;
import com.example.bookstoreapp.entity.ShoppingCart;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface ShoppingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    ShoppingCart toModel(ShoppingCartRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shoppingCart", ignore = true)
    @Mapping(target = "book", source = "book")
    CartItem toCartItem(ShoppingCartRequestDto dto, Book book);

    @Mapping(target = "bookId", source = "cartItem.book.id")
    @Mapping(target = "quantity", source = "cartItem.quantity")
    ShoppingCartResponseDto toResponseDto(CartItem cartItem);

    List<ShoppingCartDto> toDtoList(List<ShoppingCart> shoppingCarts);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    ShoppingCartItemDto toItemDto(CartItem cartItem);

    @Named("mapCartItems")
    default Set<ShoppingCartItemDto> mapCartItems(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toItemDto)
                .collect(Collectors.toSet());
    }
}
