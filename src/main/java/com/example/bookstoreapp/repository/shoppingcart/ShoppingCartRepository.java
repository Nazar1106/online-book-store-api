package com.example.bookstoreapp.repository.shoppingcart;

import com.example.bookstoreapp.entity.ShoppingCart;
import com.example.bookstoreapp.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    @Query("SELECT s FROM ShoppingCart s "
            + "LEFT JOIN FETCH s.user "
            + "LEFT JOIN FETCH s.cartItems ci "
            + "LEFT JOIN FETCH ci.book")
    List<ShoppingCart> findAllWithUserAndCartItems();

    ShoppingCart findShoppingCartByUser(User user);
}
