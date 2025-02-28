package com.example.bookstoreapp.repository.order;

import com.example.bookstoreapp.entity.Order;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"user.id", "orderItems.id"})
    List<Order> getAllByUserId(Long id, Pageable pageable);
}
