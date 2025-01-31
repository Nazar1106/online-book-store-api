package com.example.bookstoreapp.service.impl;

import com.example.bookstoreapp.dto.cartitemdto.CartItemResponseUpdateDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartRequestDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartResponseDto;
import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.entity.CartItem;
import com.example.bookstoreapp.entity.ShoppingCart;
import com.example.bookstoreapp.entity.User;
import com.example.bookstoreapp.exception.EntityNotFoundException;
import com.example.bookstoreapp.mapper.CartItemMapper;
import com.example.bookstoreapp.mapper.ShoppingMapper;
import com.example.bookstoreapp.repository.book.BookRepository;
import com.example.bookstoreapp.repository.cartitem.CartItemRepository;
import com.example.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstoreapp.repository.user.UserRepository;
import com.example.bookstoreapp.service.ShoppingCartService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    public static final String CAN_T_FIND_CART_ITEM_BY_ID_MSG = "Can't find cartItem by id ";
    public static final String CAN_T_GET_USER_BY_ID_MSG = "Can't get user by Id ";
    public static final String CAN_T_FIND_BOOK_BY_ID_MSG = "Can't find book by id";

    private final ShoppingCartRepository shoppingCartRepository;

    private final UserRepository userRepository;

    private final BookRepository bookRepository;

    private final CartItemRepository cartItemRepository;

    private final ShoppingMapper shoppingMapper;

    private final CartItemMapper cartItemMapper;

    @Override
    public List<ShoppingCartDto> getAll() {
        List<ShoppingCart> allWithUserAndCartItems = shoppingCartRepository
                .findAllWithUserAndCartItems();
        return shoppingMapper.toDtoList(allWithUserAndCartItems);
    }

    @Override
    public ShoppingCartResponseDto save(ShoppingCartRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(()
                -> new EntityNotFoundException(CAN_T_GET_USER_BY_ID_MSG + requestDto.getUserId()));
        Book book = bookRepository.findById(requestDto.getBookId()).orElseThrow(()
                -> new EntityNotFoundException(CAN_T_FIND_BOOK_BY_ID_MSG));
        CartItem cartItem = shoppingMapper.toCartItem(requestDto, book);
        cartItem.setBook(book);
        cartItem.setQuantity(requestDto.getQuantity());
        ShoppingCart shoppingCartByUser = shoppingCartRepository.findShoppingCartByUser(user);
        if (shoppingCartByUser != null) {
            cartItem.setShoppingCart(shoppingCartByUser);
            cartItemRepository.save(cartItem);
            return shoppingMapper.toResponseDto(cartItem);
        }
        ShoppingCart shoppingCart = shoppingMapper.toModel(requestDto);
        shoppingCart.setUser(user);
        shoppingCart.setCartItems(Set.of(cartItem));
        cartItem.setShoppingCart(shoppingCart);
        shoppingCartRepository.save(shoppingCart);
        return shoppingMapper.toResponseDto(cartItem);
    }

    @Override
    public CartItemResponseUpdateDto update(Long cartItemId, CartItemResponseUpdateDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException(CAN_T_FIND_CART_ITEM_BY_ID_MSG + cartItemId));
        cartItem.setQuantity(requestDto.getQuantity());
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
