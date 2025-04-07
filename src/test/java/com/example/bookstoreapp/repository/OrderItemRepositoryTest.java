package com.example.bookstoreapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bookstoreapp.entity.OrderItem;
import com.example.bookstoreapp.repository.orderitem.OrderItemRepository;
import com.example.bookstoreapp.testutil.OrderItemUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderItemRepositoryTest {

    private static final String INSERT_CATEGORIES_TO_TEST_DB_SQL =
            "database/categories/insert-categories-to-test-db.sql";
    private static final String INSERT_BOOKS_WITH_CATEGORIES_TO_DB_SQL =
            "database/books/insert-books-with-categories-to-db.sql";
    private static final String INSERT_ORDER_TO_TEST_DB_SQL =
            "database/order/insert-order-to-test-db.sql";
    private static final String INSERT_ORDER_ITEM_TO_TEST_DB_SQL =
            "database/orderitem/insert-order-item-to-test-db.sql";
    private static final String DELETE_ALL_DATA_SQL =
            "database/delete-all-data.sql";

    @Autowired
    private OrderItemRepository orderItemRepository;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource) throws SQLException {
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_CATEGORIES_TO_TEST_DB_SQL));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_BOOKS_WITH_CATEGORIES_TO_DB_SQL));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_ORDER_TO_TEST_DB_SQL));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_ORDER_ITEM_TO_TEST_DB_SQL));
        }
    }

    @Test
    @DisplayName(value = "Return optional orderItem when orderItemId and orderId and userId exist")
    void findByIdAndOrderIdAndUserId_ExistOrderItem_ShouldReturnOptionalOrderItem() {
        Long orderItemId = 1L;
        Long orderId = 1L;
        Long userId = 1L;
        Optional<OrderItem> expectedOrderItem = OrderItemUtil.getOptionalOrderItem();

        Optional<OrderItem> actualOrderItem = orderItemRepository
                .findByIdAndOrderIdAndUserId(orderItemId, orderId, userId);
        assertTrue(actualOrderItem.isPresent());
        assertTrue(expectedOrderItem.isPresent());

        OrderItem actual = actualOrderItem.get();
        OrderItem expected = expectedOrderItem.get();

        verifyOrderItemEquality(expected, actual);
    }

    @Test
    @DisplayName(value = "Return empty when orderItemId doesn't exist")
    void findByIdAndOrderIdAndUserId_NoExistOrderItemByOrderItemId_ShouldReturnEmpty() {
        Long orderItemId = 2L;
        Long orderId = 1L;
        Long userId = 1L;

        Optional<OrderItem> optionalOrderItem = orderItemRepository
                .findByIdAndOrderIdAndUserId(orderItemId, orderId, userId);

        assertTrue(optionalOrderItem.isEmpty());
    }

    @Test
    @DisplayName(value = "Return empty when orderId doesn't exist")
    void findByIdAndOrderIdAndUserId_NoExistOrderItemByOrderId_ShouldReturnEmpty() {
        Long orderItemId = 1L;
        Long orderId = 2L;
        Long userId = 1L;

        Optional<OrderItem> optionalOrderItem = orderItemRepository
                .findByIdAndOrderIdAndUserId(orderItemId, orderId, userId);

        assertTrue(optionalOrderItem.isEmpty());
    }

    @Test
    @DisplayName(value = "Return empty when userId doesn't exist")
    void findByIdAndOrderIdAndUserId_NoExistOrderItemByUserId_ShouldReturnEmpty() {
        Long orderItemId = 1L;
        Long orderId = 1L;
        Long userId = 2L;

        Optional<OrderItem> optionalOrderItem = orderItemRepository
                .findByIdAndOrderIdAndUserId(orderItemId, orderId, userId);

        assertTrue(optionalOrderItem.isEmpty());
    }

    @Test
    @DisplayName(value = "Should find one OrderItem for existing order and user")
    void findByOrderIdAndUserId_ExistSingleOrderItem_ShouldReturnPageItem() {
        Long orderId = 1L;
        Long userId = 1L;
        Page<OrderItem> expected = OrderItemUtil.getPageOrderItem();

        Pageable pageable = Pageable.ofSize(1);

        Page<OrderItem> actual =
                orderItemRepository.findByOrderIdAndUserId(orderId, userId, pageable);

        OrderItem getExpectedContent = expected.getContent().getFirst();
        OrderItem getActualContent = actual.getContent().getFirst();

        assertEquals(expected.getTotalElements(), actual.getTotalElements());
        assertEquals(expected.getTotalPages(), actual.getTotalPages());
        verifyOrderItemEquality(getExpectedContent, getActualContent);
    }

    @Test
    @DisplayName(value = "Should find two OrderItems for existing order and user")
    void findByOrderIdAndUserId_ExistTwoOrderItems_ShouldReturnPageItems() {
        Long orderId = 2L;
        Long userId = 1L;
        Pageable pageable = Pageable.ofSize(2);

        Page<OrderItem> expected = OrderItemUtil.getPageOrderItems();

        Page<OrderItem> actual =
                orderItemRepository.findByOrderIdAndUserId(orderId, userId, pageable);

        assertEquals(expected.getTotalElements(), actual.getNumberOfElements());
        assertEquals(expected.getTotalPages(), actual.getTotalPages());

        OrderItem getFirstExpectedContent = expected.getContent().getFirst();
        OrderItem getFirstActualContent = actual.getContent().getFirst();
        OrderItem getSecondExpectedContent = expected.getContent().getLast();
        OrderItem getSecondActualContent = actual.getContent().getLast();

        verifyOrderItemEquality(getFirstExpectedContent, getFirstActualContent);
        verifyOrderItemEquality(getSecondExpectedContent, getSecondActualContent);
    }

    @Test
    @DisplayName(value = "Should return an empty page for a non-existing order and user")
    void findByOrderIdAndUserId_NonExistingOrderItems_ShouldReturnEmptyPage() {
        int expectedElements = 0;
        Long orderId = 4L;
        Long userId = 4L;
        Pageable pageable = Pageable.ofSize(1);

        Page<OrderItem> pageOrderItem =
                orderItemRepository.findByOrderIdAndUserId(orderId, userId, pageable);

        assertEquals(expectedElements, pageOrderItem.getTotalElements());
    }

    private static void verifyOrderItemEquality(OrderItem expected, OrderItem actual) {
        assertEquals(expected.getOrder().getId(), actual.getOrder().getId());
        assertEquals(expected.getOrder().getId(), actual.getOrder().getId());
        assertEquals(expected.getQuantity(), actual.getQuantity());
        assertEquals(expected.getPrice(), actual.getPrice());
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
