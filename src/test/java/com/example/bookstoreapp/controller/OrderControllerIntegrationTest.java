package com.example.bookstoreapp.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstoreapp.dto.orderdto.OrderRequestDto;
import com.example.bookstoreapp.dto.orderdto.OrderUpdateDto;
import com.example.bookstoreapp.entity.User;
import com.example.bookstoreapp.testutil.OrderUtil;
import com.example.bookstoreapp.testutil.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerIntegrationTest {

    public static final String INSERT_ORDER_TO_TEST_DB_SQL =
            "database/order/insert-order-to-test-db.sql";
    public static final String INSERT_ORDER_ITEM_TO_TEST_DB_SQL =
            "database/orderitem/insert-order-item-to-test-db.sql";
    public static final String DELETE_ALL_DATA_SQL = "database/delete-all-data.sql";
    public static final String INSERT_BOOKS_WITH_CATEGORIES_TO_DB_SQL =
            "database/books/insert-books-with-categories-to-db.sql";
    public static final String CATEGORIES_TO_TEST_DB_SQL =
            "database/categories/insert-categories-to-test-db.sql";
    public static final String INSERT_SHOPPING_CART_FOR_USER_INTO_DB_SQL =
            "database/cartitems/insert-shopping-cart-for-user-into-db.sql";

    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws SQLException {
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(CATEGORIES_TO_TEST_DB_SQL));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_BOOKS_WITH_CATEGORIES_TO_DB_SQL));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_ORDER_TO_TEST_DB_SQL));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_ORDER_ITEM_TO_TEST_DB_SQL));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_SHOPPING_CART_FOR_USER_INTO_DB_SQL));
        }
    }

    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    @DisplayName("Create order with valid data")
    void createOrder_ValidData_ShouldReturnOrderResponseDto() throws Exception {
        OrderRequestDto orderRequestDto = OrderUtil.getOrderRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(orderRequestDto);

        UsernamePasswordAuthenticationToken auth = new
                UsernamePasswordAuthenticationToken(UserUtil.getUser(), null,
                UserUtil.getUser().getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        MvcResult result = mockMvc.perform(post("/orders")
                        .with(authentication(auth)).contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.status").isString())
                .andReturn();

        Assertions.assertNotNull(result);
        String jsonResponse = result.getResponse().getContentAsString();
        Assertions.assertFalse(jsonResponse.isEmpty(), "Response body should not be empty");
    }

    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    @DisplayName("Retrieve order history")
    void getUserOrderHistory_ExistData_ShouldReturnOrderResponseDto() throws Exception {

        UsernamePasswordAuthenticationToken auth = new
                UsernamePasswordAuthenticationToken(UserUtil.getUser(), null,
                UserUtil.getUser().getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        MvcResult result = mockMvc.perform(get("/orders")
                        .param("page", "0")
                        .param("size", "1")
                        .with(authentication(auth))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertNotNull(result);
        String jsonResponse = result.getResponse().getContentAsString();
        Assertions.assertFalse(jsonResponse.isEmpty(), "Response body should not be empty");
    }

    @Test
    @DisplayName("Update order status - should return updated OrderUpdateDto")
    void updateOrderStatus_ValidData_ShouldReturnUpdatedOrderDto() throws Exception {

        Long orderId = 1L;
        OrderUpdateDto updateDto = OrderUtil.getOrderUpdateDto();
        String jsonRequest = objectMapper.writeValueAsString(updateDto);

        User adminUser = UserUtil.getAdmin();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                adminUser, null, adminUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        MvcResult result = mockMvc.perform(patch("/orders/{id}", orderId)
                        .with(authentication(auth))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").isString())
                .andReturn();

        Assertions.assertNotNull(result);
        String jsonResponse = result.getResponse().getContentAsString();
        Assertions.assertFalse(jsonResponse.isEmpty(), "Response body should not be empty");
    }

    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    @DisplayName("Get order items for valid user")
    void getOrderItems_ValidUser_ShouldReturnOrderItems() throws Exception {
        Long orderId = 1L;
        UsernamePasswordAuthenticationToken auth = new
                UsernamePasswordAuthenticationToken(UserUtil.getUser(), null,
                UserUtil.getUser().getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        MvcResult result = mockMvc.perform(get("/orders/{orderId}/items", orderId)
                        .with(authentication(auth))
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andReturn();

        Assertions.assertNotNull(result);
        String jsonResponse = result.getResponse().getContentAsString();
        Assertions.assertFalse(jsonResponse.isEmpty(), "Response body should not be empty");
    }

    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    @DisplayName("Get order item by orderId and id")
    void getOrderItem_ValidData_ShouldReturnOrderItemResponseDto() throws Exception {
        Long orderId = 1L;
        Long itemId = 1L;
        User user = UserUtil.getUser();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        MvcResult result = mockMvc.perform(get("/orders/{orderId}/items/{id}", orderId, itemId)
                        .with(authentication(auth))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId))
                .andReturn();

        Assertions.assertNotNull(result);
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(DELETE_ALL_DATA_SQL));
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }
}
