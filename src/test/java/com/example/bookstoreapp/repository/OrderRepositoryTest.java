package com.example.bookstoreapp.repository;

import com.example.bookstoreapp.entity.Order;
import com.example.bookstoreapp.repository.order.OrderRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {

    public static final String ORDER_INSERT_ORDER_TO_TEST_DB_SQL =
            "database/order/insert-order-to-test-db.sql";

    public static final String DELETE_ALL_DATA_SQL =
            "database/delete-all-data.sql";

    @Autowired
    private OrderRepository orderRepository;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource) throws SQLException {
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(ORDER_INSERT_ORDER_TO_TEST_DB_SQL));
        }
    }

    @Test
    @DisplayName(value = "Return list orders when orders exist")
    void getAllByUserId_ExistOrders_ShouldReturnListOrders() {
        Long userId = 1L;
        Pageable pageable = Pageable.ofSize(2);

        List<Order> allOrdersByUserId = orderRepository
                .getAllByUserId(userId, pageable);

        int expectedOrders = 2;

        assertNotNull(allOrdersByUserId);
        assertEquals(expectedOrders, allOrdersByUserId.size());
    }

    @Test
    @DisplayName(value = "Return list with single order when order exist")
    void getAllByUserId_ExistSingleOrder_ShouldReturnListOrder() {
        Long userId = 2L;
        Pageable pageable = Pageable.ofSize(1);

        List<Order> allOrdersByUserId = orderRepository
                .getAllByUserId(userId, pageable);

        int expectedOrders = 1;

        assertNotNull(allOrdersByUserId);
        assertEquals(expectedOrders, allOrdersByUserId.size());
    }

    @Test
    @DisplayName(value = "Return empty list when order no exist")
    void getAllByUserId_NoExistOrder_ShouldReturnEmptyList() {
        Long userId = 3L;
        Pageable pageable = Pageable.ofSize(1);

        List<Order> allByUserId = orderRepository.getAllByUserId(userId, pageable);

        assertTrue(allByUserId.isEmpty());
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
