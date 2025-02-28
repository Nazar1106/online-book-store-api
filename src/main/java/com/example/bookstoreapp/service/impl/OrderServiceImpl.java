package com.example.bookstoreapp.service.impl;

import com.example.bookstoreapp.dto.orderdto.OrderDto;
import com.example.bookstoreapp.dto.orderdto.OrderRequestDto;
import com.example.bookstoreapp.dto.orderdto.OrderResponseDto;
import com.example.bookstoreapp.dto.orderdto.OrderUpdateDto;
import com.example.bookstoreapp.dto.orderitemdto.OrderItemSaveDto;
import com.example.bookstoreapp.entity.CartItem;
import com.example.bookstoreapp.entity.Order;
import com.example.bookstoreapp.entity.OrderItem;
import com.example.bookstoreapp.entity.ShoppingCart;
import com.example.bookstoreapp.entity.Status;
import com.example.bookstoreapp.entity.User;
import com.example.bookstoreapp.exception.EntityNotFoundException;
import com.example.bookstoreapp.mapper.OrderItemMapper;
import com.example.bookstoreapp.mapper.OrderMapper;
import com.example.bookstoreapp.repository.cartitem.CartItemRepository;
import com.example.bookstoreapp.repository.order.OrderRepository;
import com.example.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstoreapp.repository.user.UserRepository;
import com.example.bookstoreapp.service.OrderService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    public static final String USER_NOT_FOUND_BYD_ID_MSG = "User not found byd id ";
    public static final String CAN_T_RECEIVE_SHOPPING_CART_BY_USER_ID = "Can't receive "
            + "shopping cart by user id ";
    public static final String CAN_T_FIND_ORDER_BYD_ID = "Can't find order byd id ";

    private final UserRepository userRepository;

    private final ShoppingCartRepository shoppingCartRepository;

    private final CartItemRepository cartItemRepository;

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderDto save(Long authentication, OrderRequestDto requestDto) {

        User user = userRepository.findById(authentication).orElseThrow(()
                -> new EntityNotFoundException(USER_NOT_FOUND_BYD_ID_MSG + authentication));

        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserId(user.getId()).orElseThrow(()
                        -> new EntityNotFoundException(CAN_T_RECEIVE_SHOPPING_CART_BY_USER_ID
                        + user.getId()));

        List<CartItem> cartItems = cartItemRepository.findCartItemByShoppingCart(shoppingCart);

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(user.getShippingAddress());
        order.setStatus(Status.PENDING);
        order.setOrderDate(getLocalDataTime());
        order.setTotal(BigDecimal.ZERO);

        List<OrderItemSaveDto> orderItemDtos = cartItems.stream()
                .map(cartItem -> orderItemMapper.toOrderItemDto(cartItem, order))
                .toList();

        List<OrderItem> orderItems = orderItemMapper.toOrderItems(orderItemDtos, order);
        order.setOrderItems(new HashSet<>(orderItems));
        BigDecimal total = totalPrice(orderItems);
        order.setTotal(total);
        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderResponseDto> getAllOrderHistory(Long authentication, Pageable pageable) {
        User user = userRepository.findById(authentication).orElseThrow(
                () -> new EntityNotFoundException(USER_NOT_FOUND_BYD_ID_MSG));
        List<Order> allByUserId = orderRepository.getAllByUserId(user.getId(),pageable);
        return orderMapper.toDtos(allByUserId);
    }

    @Override
    public OrderUpdateDto update(Long authentication, Long orderId, OrderUpdateDto updateStatus) {
        userRepository.findById(authentication).orElseThrow(
                () -> new EntityNotFoundException(USER_NOT_FOUND_BYD_ID_MSG));
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException(CAN_T_FIND_ORDER_BYD_ID + orderId));
        order.setStatus(updateStatus.getStatus());
        return updateStatus;
    }

    private static LocalDateTime getLocalDataTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        int year = localDateTime.getYear();
        int month = localDateTime.getMonth().getValue();
        int day = localDateTime.getDayOfMonth();
        int hour = localDateTime.getHour();
        int minute = localDateTime.getMinute();
        int second = localDateTime.getSecond();
        return LocalDateTime.of(year, month, day, hour, minute, second);
    }

    private BigDecimal totalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
