package com.example.bookstoreapp.service.impl;

import com.example.bookstoreapp.dto.cartitemdto.CartItemRequestDto;
import com.example.bookstoreapp.dto.cartitemdto.CartItemUpdateDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartDto;
import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.entity.CartItem;
import com.example.bookstoreapp.entity.ShoppingCart;
import com.example.bookstoreapp.entity.User;
import com.example.bookstoreapp.exception.EntityNotFoundException;
import com.example.bookstoreapp.mapper.CartItemMapper;
import com.example.bookstoreapp.repository.book.BookRepository;
import com.example.bookstoreapp.repository.cartitem.CartItemRepository;
import com.example.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstoreapp.repository.user.UserRepository;
import com.example.bookstoreapp.service.ShoppingCartService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    public static final String CAN_T_GET_USER_BY_ID_MSG = "Can't get user by Id ";
    public static final String CAN_T_FIND_BOOK_BY_ID_MSG = "Can't find book by id ";
    public static final String CAN_T_FIND_CART_ITEM_BY_ID_MSG = "Can't find cart item by id ";
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getByUserId(Long authenticationId) {
        return cartItemMapper.toDto(shoppingCartRepository
                .findByUserId(authenticationId).orElseThrow(
                        () -> new EntityNotFoundException(CAN_T_GET_USER_BY_ID_MSG
                                + authenticationId)));
    }

    @Override
    public ShoppingCartDto save(Long authenticationId, CartItemRequestDto requestDto) {
        User user = userRepository.findById(authenticationId).orElseThrow(()
                -> new EntityNotFoundException(CAN_T_GET_USER_BY_ID_MSG + authenticationId));

        Book book = bookRepository.findById(requestDto.getBookId()).orElseThrow(()
                -> new EntityNotFoundException(CAN_T_FIND_BOOK_BY_ID_MSG + requestDto.getBookId()));

        ShoppingCart shoppingCartByUserId = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(CAN_T_GET_USER_BY_ID_MSG
                        + authenticationId));

        List<CartItem> list = shoppingCartByUserId
                .getCartItems().stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(book.getId())).toList();

        CartItem cartItem = new CartItem();

        if (!list.isEmpty()) {
            cartItem = list.getFirst();
            int increaseQuantity = cartItem.getQuantity() + requestDto.getQuantity();
            cartItem.setQuantity(increaseQuantity);
            return cartItemMapper.toResponseDto(cartItem);
        }
        cartItem.setBook(book);
        cartItem.setQuantity(requestDto.getQuantity());
        cartItem.setShoppingCart(shoppingCartByUserId);
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        shoppingCartByUserId.setCartItems(savedCartItem.getShoppingCart().getCartItems());
        return cartItemMapper.toDto(shoppingCartByUserId);
    }

    @Override
    public ShoppingCartDto update(Long authenticationId, Long cartItemId,
                                  CartItemUpdateDto updateDto) {
        ShoppingCart byUserId = shoppingCartRepository.findByUserId(authenticationId)
                .orElseThrow(() -> new EntityNotFoundException(CAN_T_GET_USER_BY_ID_MSG
                        + authenticationId));

        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartId(cartItemId, byUserId.getId()).orElseThrow(()
                        -> new EntityNotFoundException(CAN_T_FIND_CART_ITEM_BY_ID_MSG
                        + cartItemId));
        cartItem.setQuantity(updateDto.getQuantity());
        return cartItemMapper.toDto(byUserId);
    }

    @Override
    public void deleteById(Long authenticationId, Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(authenticationId)
                .orElseThrow(() -> new EntityNotFoundException(CAN_T_GET_USER_BY_ID_MSG
                        + authenticationId));
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartId(id, shoppingCart.getId()).orElseThrow(()
                        -> new EntityNotFoundException(CAN_T_FIND_CART_ITEM_BY_ID_MSG + id));
        shoppingCart.getCartItems().remove(cartItem);
    }

    @Override
    public void saveShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
}
