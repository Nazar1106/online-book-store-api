package com.example.bookstoreapp.mapper;

import com.example.bookstoreapp.config.MapperConfig;
import com.example.bookstoreapp.dto.orderdto.OrderDto;
import com.example.bookstoreapp.dto.orderdto.OrderResponseDto;
import com.example.bookstoreapp.dto.orderitemdto.OrderItemResponseDto;
import com.example.bookstoreapp.entity.Order;
import com.example.bookstoreapp.entity.OrderItem;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderItems", target = "orderItems", qualifiedByName = "mapOrderItems")
    OrderResponseDto toDto(Order order);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "userId", target = "user.id")
    Order toModel(OrderDto orderDto);

    List<OrderResponseDto> toDtos(List<Order> orders);

    @Named(value = "mapOrderItems")
    default List<OrderItemResponseDto> mapOrderItems(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> {
                    OrderItemResponseDto dto = new OrderItemResponseDto();
                    dto.setId(item.getId());
                    dto.setBookId(item.getBook().getId());
                    dto.setQuantity(item.getQuantity());
                    return dto;
                })
                .toList();
    }
}
