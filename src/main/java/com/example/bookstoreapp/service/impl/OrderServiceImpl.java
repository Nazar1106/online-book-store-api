package com.example.bookstoreapp.service.impl;

import com.example.bookstoreapp.dto.orderdto.OrderRequestDto;
import com.example.bookstoreapp.dto.orderdto.OrderResponseDto;
import com.example.bookstoreapp.dto.orderdto.OrderUpdateDto;
import com.example.bookstoreapp.dto.orderitemdto.OrderItemResponseDto;
import com.example.bookstoreapp.entity.Order;
import com.example.bookstoreapp.entity.ShoppingCart;
import com.example.bookstoreapp.exception.EntityNotFoundException;
import com.example.bookstoreapp.exception.OrderProcessingException;
import com.example.bookstoreapp.mapper.OrderItemMapper;
import com.example.bookstoreapp.mapper.OrderMapper;
import com.example.bookstoreapp.repository.order.OrderRepository;
import com.example.bookstoreapp.repository.orderitem.OrderItemRepository;
import com.example.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstoreapp.service.OrderService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    public static final String CAN_T_FIND_ORDER_BYD_ID = "Can't find order byd id ";
    public static final String ORDER_ITEM_NOT_FOUND_ = "Order item not found ";
    public static final String CAN_T_PLACE_AN_ORDER = "The cart is empty, "
            + "so you can't place an order";

    private final ShoppingCartRepository shoppingCartRepository;

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final OrderItemMapper orderItemMapper;

    private final OrderItemRepository orderItemRepository;

    public OrderResponseDto createOrder(Long userId, OrderRequestDto orderDto) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId).orElseThrow();
        if (cart.getCartItems().isEmpty()) {
            throw new OrderProcessingException(CAN_T_PLACE_AN_ORDER + userId);
        }
        Order order = orderMapper.cartToOrder(cart, orderDto.getShippingAddress());
        cart.clearCart();
        return orderMapper.toOrderDto(orderRepository.save(order));
    }

    @Override
    public List<OrderResponseDto> getUserOrderHistory(Long userId, Pageable pageable) {
        List<Order> allByUserId = orderRepository.getAllByUserId(userId, pageable);
        return orderMapper.toOrderDtoList(allByUserId);
    }

    @Override
    public OrderUpdateDto updateOrderStatus(Long userId, Long orderId,
                                            OrderUpdateDto updateStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException(CAN_T_FIND_ORDER_BYD_ID + orderId));
        order.setStatus(updateStatus.getStatus());
        return updateStatus;
    }

    @Override
    public Page<OrderItemResponseDto> getOrderItems(Long userId, Long orderId,
                                                                Pageable pageable) {

        return orderItemRepository.findByOrderIdAndUserId(orderId, userId, pageable)
                .map(orderItemMapper::toDto);
    }

    @Override
    public OrderItemResponseDto getOrderItem(Long userId, Long orderId, Long itemId) {
        return orderItemRepository
                .findByIdAndOrderIdAndUserId(itemId, orderId, userId)
                .map(orderItemMapper::toDto).orElseThrow(
                        () -> new EntityNotFoundException(ORDER_ITEM_NOT_FOUND_));
    }
}
