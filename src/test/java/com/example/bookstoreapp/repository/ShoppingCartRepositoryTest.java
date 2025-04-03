package com.example.bookstoreapp.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bookstoreapp.entity.ShoppingCart;
import com.example.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import java.sql.Connection;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {

    private static final String DELETE_ALL_DATA_SQL = "database/delete-all-data.sql";

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName(value = "Should return optional when shoppingCart exists")
     void findByUserId_ShoppingCartExist_ShouldReturnShoppingCart() {
        Long userId = 1L;

        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserId(userId);

        Assertions.assertNotNull(shoppingCart);
    }

    @Test
    @DisplayName(value = "Should return optional empty when shoppingCart doesn't exist")
     void findByUserId_UserIdNoExist_ShouldReturnOptionalEmpty() {
        Long userId = 999L;

        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserId(userId);

        assertTrue(shoppingCart.isEmpty(), "Shopping cart should be empty");
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        tearDown(dataSource);
    }

    @SneakyThrows
    static void tearDown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(DELETE_ALL_DATA_SQL));
        }
    }
}
