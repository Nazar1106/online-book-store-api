package com.example.bookstoreapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bookstoreapp.entity.Order;
import com.example.bookstoreapp.repository.order.OrderRepository;
import com.example.bookstoreapp.testutil.OrderUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {

    private static final String ORDER_INSERT_ORDER_TO_TEST_DB_SQL =
            "database/order/insert-order-to-test-db.sql";

    private static final String DELETE_ALL_DATA_SQL =
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
        List<Order> expectedListOrders = OrderUtil.getListOrders();

        List<Order> actualListOrders = orderRepository
                .getAllByUserId(userId, pageable);

        Order expectedGetFirst = expectedListOrders.getFirst();
        Order expectedGetSecond = expectedListOrders.getLast();

        Order actualGetFirst = actualListOrders.getFirst();
        Order actualGetSecond = actualListOrders.getLast();

        expectedGetFirst.setOrderDate(actualGetFirst.getOrderDate());
        expectedGetSecond.setOrderDate(actualGetSecond.getOrderDate());

        assertNotNull(actualListOrders);
        assertEquals(expectedListOrders.size(), actualListOrders.size());

        verifyOrderEquality(expectedGetFirst, actualGetFirst);
        verifyOrderEquality(expectedGetSecond, actualGetSecond);

    }

    @Test
    @DisplayName(value = "Return list with single order when order exist")
    void getAllByUserId_ExistSingleOrder_ShouldReturnListOrder() {
        Long userId = 2L;
        Pageable pageable = Pageable.ofSize(1);

        List<Order> expectedListOrder = OrderUtil.getListOrder();

        List<Order> actualListOrder = orderRepository
                .getAllByUserId(userId, pageable);

        Order expectedOrder = expectedListOrder.getFirst();
        Order actualOrder = actualListOrder.getFirst();
        expectedOrder.setOrderDate(actualOrder.getOrderDate());

        assertNotNull(actualListOrder);
        assertEquals(expectedListOrder.size(), actualListOrder.size());
        verifyOrderEquality(expectedOrder, actualOrder);
    }

    @Test
    @DisplayName(value = "Return empty list when order no exist")
    void getAllByUserId_NoExistOrder_ShouldReturnEmptyList() {
        Long userId = 3L;
        Pageable pageable = Pageable.ofSize(1);

        List<Order> allByUserId = orderRepository.getAllByUserId(userId, pageable);

        assertTrue(allByUserId.isEmpty());
    }

    private static void verifyOrderEquality(Order expectedGetFirst, Order actualGetFirst) {
        assertEquals(expectedGetFirst.getId(), actualGetFirst.getId());
        assertEquals(expectedGetFirst.getOrderDate(), actualGetFirst.getOrderDate());
        assertEquals(expectedGetFirst.getStatus(), actualGetFirst.getStatus());
        assertEquals(expectedGetFirst.getTotal(), actualGetFirst.getTotal());
        assertEquals(expectedGetFirst.getShippingAddress(), actualGetFirst.getShippingAddress());
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
