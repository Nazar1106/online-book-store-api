package com.example.bookstoreapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bookstoreapp.entity.ShoppingCart;
import com.example.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstoreapp.testutil.ShoppingCartUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
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
    private static final String INSERT_SHOPPING_CART_FOR_USER_INTO_DB_SQL =
            "database/shoppingcarts/insert-shopping-cart-for-user-into-db.sql";
    private static final String INSERT_BOOKS_WITH_CATEGORIES_TO_DB_SQL =
            "database/books/insert-books-with-categories-to-db.sql";
    private static final String CATEGORIES_TO_TEST_DB_SQL =
            "database/categories/insert-categories-to-test-db.sql";

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws SQLException {
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(CATEGORIES_TO_TEST_DB_SQL));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_BOOKS_WITH_CATEGORIES_TO_DB_SQL));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_SHOPPING_CART_FOR_USER_INTO_DB_SQL));
        }
    }

    @Test
    @DisplayName(value = "Should return optional when shoppingCart exists")
     void findByUserId_ShoppingCartExist_ShouldReturnShoppingCart() {
        Long userId = 1L;
        ShoppingCart expectedShoppingCart = ShoppingCartUtil.getExpectedShoppingCart();

        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserId(userId);
        assertTrue(shoppingCart.isPresent());

        ShoppingCart actualShoppingCart = shoppingCart.get();
        assertEquals(expectedShoppingCart.getUser().getId(), actualShoppingCart.getUser().getId());
        assertEquals(expectedShoppingCart.getId(), actualShoppingCart.getId());
        assertEquals(expectedShoppingCart.isDeleted(), actualShoppingCart.isDeleted());
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
