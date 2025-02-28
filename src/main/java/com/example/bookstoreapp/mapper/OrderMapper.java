package com.example.bookstoreapp.mapper;

import com.example.bookstoreapp.config.MapperConfig;
import com.example.bookstoreapp.dto.orderdto.OrderDto;
import com.example.bookstoreapp.dto.orderdto.OrderResponseDto;
import com.example.bookstoreapp.dto.orderitemdto.OrderItemDto;
import com.example.bookstoreapp.dto.orderitemdto.OrderItemSaveDto;
import com.example.bookstoreapp.entity.Order;
import com.example.bookstoreapp.entity.OrderItem;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    OrderDto toDto(Order order);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "order.id", target = "orderId")
    OrderItemSaveDto toDto(OrderItem orderItem);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderItems", target = "orderItems", qualifiedByName = "mapOrderItems")
    OrderResponseDto toEntity(Order order);

    List<OrderResponseDto> toDtos(List<Order> orders);

    @Named("mapOrderItems")
    default List<OrderItemDto> mapOrderItems(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> {
                    OrderItemDto dto = new OrderItemDto();
                    dto.setId(item.getId());
                    dto.setBookId(item.getBook().getId());
                    dto.setQuantity(item.getQuantity());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
