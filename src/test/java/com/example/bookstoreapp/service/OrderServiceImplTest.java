package com.example.bookstoreapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookstoreapp.OrderItemUtil;
import com.example.bookstoreapp.OrderUtil;
import com.example.bookstoreapp.ShoppingCartUtil;
import com.example.bookstoreapp.dto.orderdto.OrderRequestDto;
import com.example.bookstoreapp.dto.orderdto.OrderResponseDto;
import com.example.bookstoreapp.dto.orderdto.OrderUpdateDto;
import com.example.bookstoreapp.dto.orderitemdto.OrderItemResponseDto;
import com.example.bookstoreapp.entity.Order;
import com.example.bookstoreapp.entity.OrderItem;
import com.example.bookstoreapp.entity.ShoppingCart;
import com.example.bookstoreapp.exception.EntityNotFoundException;
import com.example.bookstoreapp.exception.OrderProcessingException;
import com.example.bookstoreapp.mapper.OrderItemMapper;
import com.example.bookstoreapp.mapper.OrderMapper;
import com.example.bookstoreapp.repository.order.OrderRepository;
import com.example.bookstoreapp.repository.orderitem.OrderItemRepository;
import com.example.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstoreapp.service.impl.OrderServiceImpl;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private OrderItemRepository orderItemRepository;
    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderRequestDto orderRequestDto;

    private OrderUpdateDto orderUpdateDto;

    private OrderResponseDto orderResponseDto;

    private Order order;

    private OrderItem orderItem;

    private OrderItemResponseDto orderItemResponseDto;

    private ShoppingCart shoppingCartWithItems;

    private ShoppingCart shoppingCartWithoutItems;

    @BeforeEach
    void setUp() {
        orderRequestDto = OrderUtil.getOrderRequestDto();
        orderUpdateDto = OrderUtil.getOrderUpdateDto();
        shoppingCartWithItems = ShoppingCartUtil.getShoppingCartWithItems();
        shoppingCartWithoutItems = ShoppingCartUtil.getShoppingCartWithoutItems();
        order = OrderUtil.getOrder();
        orderResponseDto = OrderUtil.getOrderResponseDto();
        orderItem = OrderItemUtil.getOrderItem();
        orderItemResponseDto = OrderItemUtil.getOrderItemResponseDto();
    }

    @Test
    @DisplayName("Create order with valid input should return OrderResponseDto")
    void createOrder_ValidOrder_ShouldReturnOrderResponseDto() {
        Long userId = 1L;
        String actualShippingAddress = order.getShippingAddress();

        when(shoppingCartRepository.findByUserId(userId))
                .thenReturn(Optional.of(shoppingCartWithItems));
        when(orderMapper.cartToOrder(shoppingCartWithItems,
                actualShippingAddress)).thenReturn(order);

        shoppingCartWithItems.setCartItems(new HashSet<>(shoppingCartWithItems.getCartItems()));
        when(orderMapper.toOrderDto(orderRepository.save(order))).thenReturn(orderResponseDto);

        OrderResponseDto savedOrder = orderService.createOrder(userId, orderRequestDto);

        assertNotNull(savedOrder);
        assertEquals(orderResponseDto.getUserId(), userId);
        assertEquals(orderResponseDto.getTotal(), order.getTotal());
        assertEquals(orderResponseDto.getStatus(), order.getStatus().name());
        assertTrue(shoppingCartWithItems.getCartItems().isEmpty(),
                "Shopping cart should be empty after the order is created.");

        verify(shoppingCartRepository).findByUserId(userId);
        verify(orderMapper).cartToOrder(shoppingCartWithItems, actualShippingAddress);
    }

    @Test
    @DisplayName("Create order with invalid input should throw OrderProcessingException")
    void createOrder_NotValidOrder_ShouldThrowOrderProcessingException() {
        Long userId = 1L;

        when(shoppingCartRepository.findByUserId(userId))
                .thenReturn(Optional.of(shoppingCartWithoutItems));

        OrderProcessingException exception = assertThrows(OrderProcessingException.class, () ->
                orderService.createOrder(userId, orderRequestDto)
        );

        String expectedMessage = "The cart is empty, "
                + "so you can't place an order " + userId;

        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Get user order history when orders exist should return a list of orders")
    void getUserOrderHistory_ExistOrders_ShouldReturnListOrders() {
        Long userId = 1L;

        Pageable pageable = Pageable.ofSize(1);

        when(orderRepository.getAllByUserId(userId, pageable)).thenReturn(List.of(order));
        when(orderMapper.toOrderDtoList(List.of(order))).thenReturn(List.of(orderResponseDto));

        List<OrderResponseDto> orderHistory = orderService.getUserOrderHistory(userId, pageable);

        int expectedSize = 1;

        assertNotNull(orderHistory);
        assertEquals(expectedSize, orderHistory.size());
    }

    @Test
    @DisplayName("Update order status with valid input should return OrderUpdateDto")
    void updateOrderStatus_ValidOrderUpdateStatus_ShouldReturnOrderUpdateDto() {
        Long userId = 1L;
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderUpdateDto updatedDto = orderService.updateOrderStatus(userId, orderId, orderUpdateDto);

        String expectedStatus = "DELIVERED";

        assertNotNull(updatedDto);
        Assertions.assertEquals(expectedStatus, updatedDto.getStatus().name());
    }

    @Test
    @DisplayName("Update order status with invalid order should throw EntityNotFoundException")
    void updateOrderStatus_NotValidOrder_ShouldThrowEntityNotFoundException() {
        Long userId = 1L;
        Long orderId = 1L;

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                orderService.updateOrderStatus(userId, orderId, orderUpdateDto));

        String expectedMessage = "Can't find order byd id " + orderId;

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Get order items for valid order and user ID should return OrderItemResponseDto")
    void getOrderItems_ValidOrderAndUserId_ShouldReturnOrderItemResponseDto() {
        Long userId = 1L;
        Long orderId = 1L;
        Pageable pageable = Pageable.ofSize(1);
        Page<OrderItem> page = new PageImpl<>(List.of(orderItem));

        when(orderItemMapper.toDto(orderItem)).thenReturn(orderItemResponseDto);
        when(orderItemRepository.findByOrderIdAndUserId(orderId, userId, pageable))
                .thenReturn(page);

        Page<OrderItemResponseDto> items = orderService.getOrderItems(userId, orderId, pageable);

        Assertions.assertNotNull(items);
        Assertions.assertEquals(1, items.getContent().size());
    }

    @Test
    @DisplayName("Get specific order item for valid user, order, and item ID "
            + "should return OrderItemResponseDto")
    void getOrderItem_ValidUserOrderAndItemId_ShouldReturnOrderItemResponseDto() {
        Long userId = 1L;
        Long orderId = 1L;
        Long orderItemId = 1L;

        when(orderItemMapper.toDto(orderItem)).thenReturn(orderItemResponseDto);
        when(orderItemRepository.findByIdAndOrderIdAndUserId(orderItemId, orderId, userId))
                .thenReturn(Optional.of(orderItem));

        OrderItemResponseDto itemResponseDto = orderService
                .getOrderItem(userId, orderId, orderItemId);

        assertNotNull(itemResponseDto);
        assertEquals(orderItem.getId(), itemResponseDto.getId());

        verify(orderItemMapper).toDto(orderItem);
        verify(orderItemRepository).findByIdAndOrderIdAndUserId(orderItemId, orderId, userId);
    }

    @Test
    @DisplayName("Get order item with invalid order item ID "
            + "should throw EntityNotFoundException")
    void getOrderItem_NotValidOrderItemId_ShouldThrowEntityNotFoundException() {
        Long userId = 1L;
        Long orderId = 1L;
        Long orderItemId = 999L;

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                orderService.getOrderItem(userId, orderId, orderItemId));

        String expectedMessage = "Order item not found ";

        assertEquals(expectedMessage, exception.getMessage());
    }
}
