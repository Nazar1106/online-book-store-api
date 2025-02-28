package com.example.bookstoreapp.repository.orderitem;

import com.example.bookstoreapp.entity.OrderItem;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @EntityGraph(attributePaths = "book")
    List<OrderItem> findByOrderId(Long orderId, Pageable pageable);

    @EntityGraph(attributePaths = "book")
    Page<OrderItem> findByIdAndAndOrderId(Long itemId, Long orderId, Pageable pageable);
}
