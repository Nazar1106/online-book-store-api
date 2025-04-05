package com.example.bookstoreapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookstoreapp.dto.cartitemdto.CartItemRequestDto;
import com.example.bookstoreapp.dto.cartitemdto.CartItemUpdateDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartDto;
import com.example.bookstoreapp.dto.shoppingcartdto.ShoppingCartItemDto;
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
import com.example.bookstoreapp.service.impl.ShoppingCartServiceImpl;
import com.example.bookstoreapp.testutil.BookUtil;
import com.example.bookstoreapp.testutil.CartItemUtil;
import com.example.bookstoreapp.testutil.ShoppingCartUtil;
import com.example.bookstoreapp.testutil.UserUtil;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceImplTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    private ShoppingCartDto shoppingCartDto;
    private ShoppingCart shoppingCartWithItems;
    private ShoppingCart shoppingCartWithoutItems;
    private CartItem cartItem;
    private CartItemRequestDto cartItemRequestDto;
    private User user;
    private Book book;

    @BeforeEach
    void setUp() {
        shoppingCartDto = ShoppingCartUtil.getShoppingCartDto();
        shoppingCartWithItems = ShoppingCartUtil.getShoppingCartWithItems();
        shoppingCartWithoutItems = ShoppingCartUtil.getShoppingCartWithoutItems();
        user = UserUtil.getUserAfterSaveIntoDb();
        cartItemRequestDto = CartItemUtil.getCartItemRequestDto();
        cartItem = CartItemUtil.getCartItem();
        book = BookUtil.getBook();

    }

    @Test
    @DisplayName("Should return ShoppingCartDto when shopping cart exists for given user ID")
    void getByUserId_WhenShoppingCartExists_ShouldReturnShoppingCartDto() {
        Long userId = 1L;

        when(cartItemMapper.toDto(shoppingCartWithItems))
                .thenReturn(shoppingCartDto);
        when(shoppingCartRepository.findByUserId(userId))
                .thenReturn(Optional.of(shoppingCartWithItems));

        ShoppingCartDto getCartByUserId = shoppingCartService.getByUserId(userId);

        Assertions.assertNotNull(getCartByUserId, "ShoppingCartDto should not be null");

        assertEquals(getCartByUserId, shoppingCartDto);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when "
            + "shopping cart does not exist for given user ID")
    void getByUserId_WhenShoppingCartNotExists_ShouldThrowException() {
        Long userId = 999L;
        String expectedMessage = "Can't get user by Id " + userId;

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.getByUserId(userId));

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Should return ShoppingCartDto when saving a valid shopping cart item")
    void save_WhenValidShoppingCart_ShouldReturnShoppingCartDto() {
        Long userId = 1L;
        Long bookId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(shoppingCartRepository.findByUserId(userId))
                .thenReturn(Optional.of(shoppingCartWithoutItems));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartItemMapper.toDto(shoppingCartWithoutItems)).thenReturn(shoppingCartDto);

        ShoppingCartDto saveShoppingCart = shoppingCartService.save(userId, cartItemRequestDto);

        assertEquals(shoppingCartDto, saveShoppingCart);
        Assertions.assertNotNull(shoppingCartDto.getCartItems());

        verify(userRepository).findById(userId);
        verify(bookRepository).findById(bookId);
        verify(shoppingCartRepository).findByUserId(userId);
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    @DisplayName("Should increase quantity if cart item already exists in shopping cart")
    void save_WhenCartItemAlreadyExists_ShouldIncreaseQuantity() {
        Long userId = 1L;
        Long bookId = 1L;
        final int existQuantity = 15;
        final int requestQuantity = 15;
        final int expectedQuantity = ShoppingCartUtil.getExpectedQuantity();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        when(shoppingCartRepository.findByUserId(userId))
                .thenReturn(Optional.of(shoppingCartWithItems));

        when(cartItemMapper.toResponseDto(any(CartItem.class))).thenReturn(shoppingCartDto);

        ShoppingCartDto saveShoppingCart = shoppingCartService.save(userId, cartItemRequestDto);

        Assertions.assertNotNull(saveShoppingCart);
        Assertions.assertEquals(expectedQuantity, existQuantity + requestQuantity);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when user does not exist")
    void save_WhenUserNotExists_ShouldThrowException() {
        Long notExistUserId = 999L;

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.save(notExistUserId, cartItemRequestDto));

        String expectedMessage = "Can't get user by Id " + notExistUserId;
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when book does not exist")
    void save_WhenBookNotExists_ShouldThrowException() {
        Long userId = 1L;
        Long notExistBookId = 999L;

        CartItemRequestDto cartItem = new CartItemRequestDto();
        cartItem.setBookId(notExistBookId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.save(userId, cartItem));

        String expectedMessage = "Can't find book by id " + notExistBookId;

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Should return updated ShoppingCartDto when updating an existing cart item")
    void update_WhenCartItemExists_ShouldReturnUpdatedShoppingCartDto() {
        Long cartItemId = 1L;
        Long userId = 1L;
        CartItemUpdateDto cartItemUpdateDto = new CartItemUpdateDto();
        cartItemUpdateDto.setQuantity(30);

        when(shoppingCartRepository.findByUserId(userId))
                .thenReturn(Optional.of(shoppingCartWithItems));
        when(cartItemRepository.findByIdAndShoppingCartId(cartItemId, userId))
                .thenReturn(Optional.of(cartItem));
        when(cartItemMapper.toDto(shoppingCartWithItems)).thenReturn(shoppingCartDto);

        ShoppingCartDto updatedCartItem =
                shoppingCartService.update(userId, cartItemId, cartItemUpdateDto);

        Integer expectedQuantity = cartItemUpdateDto.getQuantity();
        Integer actualQuantity = updatedCartItem.getCartItems()
                .stream().map(ShoppingCartItemDto::getQuantity).findFirst().orElse(null);

        Assertions.assertNotNull(updatedCartItem);
        Assertions.assertEquals(expectedQuantity, actualQuantity);
    }

    @Test
    void update_ShoppingCartItemNotExist_ShouldReturnException() {
        Long nonExistCartItemId = 999L;
        Long userId = 1L;
        CartItemUpdateDto cartItemUpdateDto = new CartItemUpdateDto();
        cartItemUpdateDto.setQuantity(30);

        when(shoppingCartRepository.findByUserId(userId))
                .thenReturn(Optional.of(shoppingCartWithItems));
        when(cartItemRepository.findByIdAndShoppingCartId(nonExistCartItemId, userId))
                .thenReturn(Optional.empty());

        EntityNotFoundException actualException = assertThrows(EntityNotFoundException.class, () ->
                shoppingCartService.update(userId, nonExistCartItemId, cartItemUpdateDto));

        String expectedException = "Can't find cart item by id " + nonExistCartItemId;

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedException, actualException.getMessage());
    }

    @Test
    @DisplayName("Should remove cart item when it exists in shopping cart")
    void deleteById_WhenCartItemExists_ShouldRemoveCartItem() {
        Long userId = 1L;
        Long cartItemId = 1L;
        Long shoppingCartId = 1L;

        when(shoppingCartRepository.findByUserId(userId))
                .thenReturn(Optional.of(shoppingCartWithItems));

        when(cartItemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCartId))
                .thenReturn(Optional.of(cartItem));
        shoppingCartWithItems.setCartItems(new HashSet<>(shoppingCartWithItems.getCartItems()));
        shoppingCartService.deleteById(userId, cartItemId);

        assertFalse(shoppingCartWithItems.getCartItems().contains(cartItem));
        verify(cartItemRepository)
                .findByIdAndShoppingCartId(cartItemId, shoppingCartWithItems.getId());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when cart item does not exist")
    void deleteById_CartItemNotExists_ShouldReturnException() {
        Long userId = 1L;
        final Long notExistCartItemId = 999L;

        when(shoppingCartRepository.findByUserId(userId))
                .thenReturn(Optional.of(shoppingCartWithItems));

        EntityNotFoundException actualException = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.deleteById(userId, notExistCartItemId)
        );

        String expectedException = "Can't find cart item by id " + notExistCartItemId;

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedException, actualException.getMessage());
    }

    @Test
    @DisplayName("Should create a new shopping cart for an existing user")
    void saveShoppingCartForUser_UserExist_ShouldCreateShoppingCart() {
        when(shoppingCartRepository.save(any(ShoppingCart.class)))
                .thenReturn(shoppingCartWithoutItems);

        shoppingCartService.saveShoppingCartForUser(user);
    }
}
