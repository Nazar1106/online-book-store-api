package com.example.bookstoreapp.service.impl;

import com.example.bookstoreapp.dto.orderitemdto.OrderItemDto;
import com.example.bookstoreapp.entity.OrderItem;
import com.example.bookstoreapp.exception.EntityNotFoundException;
import com.example.bookstoreapp.mapper.OrderItemMapper;
import com.example.bookstoreapp.repository.orderitem.OrderItemRepository;
import com.example.bookstoreapp.repository.user.UserRepository;
import com.example.bookstoreapp.service.OrderItemService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderItemServiceImpl implements OrderItemService {

    public static final String CAN_T_FIND_USER_BY_USED_ID = "Can't find user by used id ";

    private final OrderItemRepository orderItemRepository;

    private final UserRepository userRepository;

    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItemDto> getAllByOrderId(Long authentication, Long orderId,
                                              Pageable pageable) {
        userRepository.findById(authentication).orElseThrow(
                () -> new EntityNotFoundException(CAN_T_FIND_USER_BY_USED_ID + authentication));
        List<OrderItem> byOrderId = orderItemRepository
                .findByOrderId(orderId, pageable);
        return orderItemMapper.toDtos(byOrderId);
    }

    @Override
    public Page<OrderItemDto> getBySpecificId(Long authentication, Long orderId, Long itemId,
                                              Pageable pageable) {
        userRepository.findById(authentication).orElseThrow(
                () -> new EntityNotFoundException(CAN_T_FIND_USER_BY_USED_ID));
        return orderItemRepository.findByIdAndAndOrderId(itemId, orderId, pageable)
                .map(orderItemMapper::toDto);
    }
}
