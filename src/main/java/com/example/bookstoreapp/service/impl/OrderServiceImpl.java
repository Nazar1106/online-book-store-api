package com.example.bookstoreapp.service.impl;

import com.example.bookstoreapp.dto.orderdto.OrderDto;
import com.example.bookstoreapp.dto.orderdto.OrderRequestDto;
import com.example.bookstoreapp.dto.orderdto.OrderResponseDto;
import com.example.bookstoreapp.dto.orderdto.OrderUpdateDto;
import com.example.bookstoreapp.dto.orderitemdto.OrderItemDto;
import com.example.bookstoreapp.dto.orderitemdto.OrderItemResponseDto;
import com.example.bookstoreapp.entity.CartItem;
import com.example.bookstoreapp.entity.Order;
import com.example.bookstoreapp.entity.OrderItem;
import com.example.bookstoreapp.entity.ShoppingCart;
import com.example.bookstoreapp.entity.Status;
import com.example.bookstoreapp.exception.EntityNotFoundException;
import com.example.bookstoreapp.mapper.OrderItemMapper;
import com.example.bookstoreapp.mapper.OrderMapper;
import com.example.bookstoreapp.repository.cartitem.CartItemRepository;
import com.example.bookstoreapp.repository.order.OrderRepository;
import com.example.bookstoreapp.repository.orderitem.OrderItemRepository;
import com.example.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstoreapp.service.OrderService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    public static final String CAN_T_RECEIVE_SHOPPING_CART_BY_USER_ID = "Can't receive "
            + "shopping cart by user id ";
    public static final String CAN_T_FIND_ORDER_BYD_ID = "Can't find order byd id ";
    public static final String ORDER_ITEM_NOT_FOUND_ = "Order item not found ";
    public static final String CAN_T_PLACE_AN_ORDER = "The cart is empty, "
            + "so you can't place an order";

    private final ShoppingCartRepository shoppingCartRepository;

    private final CartItemRepository cartItemRepository;

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final OrderItemMapper orderItemMapper;

    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderResponseDto save(Long userId, OrderRequestDto requestDto) {

        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserId(userId).orElseThrow(()
                        -> new EntityNotFoundException(CAN_T_RECEIVE_SHOPPING_CART_BY_USER_ID
                        + userId));

        if (shoppingCart.getCartItems().isEmpty()) {
            throw new EntityNotFoundException(CAN_T_PLACE_AN_ORDER);
        }

        List<CartItem> cartItems =
                cartItemRepository.findCartItemByShoppingCart(shoppingCart);
        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(userId);
        orderDto.setShippingAddress(shoppingCart.getUser().getShippingAddress());
        orderDto.setStatus(Status.PENDING);
        orderDto.setOrderDate(getLocalDataTime());
        orderDto.setTotal(BigDecimal.ZERO);

        Set<OrderItemDto> orderItemsDtos = orderItemMapper.toDtos(cartItems);
        double totalAmount = getTotal(orderItemsDtos);
        orderDto.setTotal(BigDecimal.valueOf(totalAmount));
        Set<OrderItem> orderItems = orderItemMapper.toModels(orderItemsDtos);
        Order order = orderMapper.toModel(orderDto);
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        order.setOrderItems(orderItems);
        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderResponseDto> getAllOrderHistory(Long userId, Pageable pageable) {
        List<Order> allByUserId = orderRepository.getAllByUserId(userId, pageable);
        return orderMapper.toDtos(allByUserId);
    }

    @Override
    public OrderUpdateDto update(Long userId, Long orderId, OrderUpdateDto updateStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException(CAN_T_FIND_ORDER_BYD_ID + orderId));
        order.setStatus(updateStatus.getStatus());
        return updateStatus;
    }

    @Override
    public Page<OrderItemResponseDto> getAllOrderItemsByOrderId(Long userId, Long orderId,
                                                                Pageable pageable) {
        return orderItemRepository.findByOrderIdAndUserId(orderId, userId, pageable)
                .map(orderItemMapper::toDto);
    }

    @Override
    public OrderItemResponseDto getBySpecificId(Long userId, Long orderId, Long itemId) {
        return orderItemRepository
                .findByIdAndOrderIdAndUserId(itemId, orderId, userId)
                .map(orderItemMapper::toDto).orElseThrow(
                        () -> new EntityNotFoundException(ORDER_ITEM_NOT_FOUND_));
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

    private static double getTotal(Set<OrderItemDto> orderItemsDtos) {
        return orderItemsDtos.stream().map(OrderItemDto::getPrice)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
    }
}
