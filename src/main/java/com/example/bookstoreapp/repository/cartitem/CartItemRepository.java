package com.example.bookstoreapp.repository.cartitem;

import com.example.bookstoreapp.entity.CartItem;
import com.example.bookstoreapp.entity.ShoppingCart;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByIdAndShoppingCartId(Long id, Long shoppingCartId);

    List<CartItem> findCartItemByShoppingCart(ShoppingCart shoppingCart);
}
