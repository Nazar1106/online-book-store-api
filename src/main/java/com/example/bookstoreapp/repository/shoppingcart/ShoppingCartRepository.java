package com.example.bookstoreapp.repository.shoppingcart;

import com.example.bookstoreapp.entity.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    @EntityGraph(attributePaths = {"cartItems", "cartItems.book", "user"})
    Optional<ShoppingCart> findByUserId(Long userId);
}
