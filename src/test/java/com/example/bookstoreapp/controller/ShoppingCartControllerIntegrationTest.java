package com.example.bookstoreapp.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstoreapp.dto.cartitemdto.CartItemRequestDto;
import com.example.bookstoreapp.entity.ShoppingCart;
import com.example.bookstoreapp.entity.User;
import com.example.bookstoreapp.testutil.CartItemUtil;
import com.example.bookstoreapp.testutil.ShoppingCartUtil;
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
public class ShoppingCartControllerIntegrationTest {

    public static final String INSERT_SHOPPING_CART_FOR_USER_INTO_DB_SQL =
            "database/cartitems/insert-shopping-cart-for-user-into-db.sql";
    public static final String INSERT_BOOKS_WITH_CATEGORIES_TO_DB_SQL =
            "database/books/insert-books-with-categories-to-db.sql";
    public static final String CATEGORIES_TO_TEST_DB_SQL =
            "database/categories/insert-categories-to-test-db.sql";
    public static final String DELETE_ALL_DATA_SQL =
            "database/delete-all-data.sql";

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
                    new ClassPathResource(INSERT_SHOPPING_CART_FOR_USER_INTO_DB_SQL));
        }
    }

    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    @DisplayName("Get shopping cart by userId")
    void getByUserId_ValidUser_ShouldReturnShoppingCartDto() throws Exception {
        User user = UserUtil.getUser();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        ShoppingCart cartWithItems = ShoppingCartUtil.getShoppingCart();

        String jsonRequest = objectMapper.writeValueAsString(cartWithItems);

        MvcResult result = mockMvc.perform(get("/cart")
                        .with(authentication(auth))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems").isArray())
                .andExpect(jsonPath("$.cartItems[0].id").exists())
                .andExpect(jsonPath("$.cartItems[0].bookId").exists())
                .andExpect(jsonPath("$.cartItems[0].bookTitle").exists())
                .andExpect(jsonPath("$.cartItems[0].quantity").exists())
                .andReturn();

        Assertions.assertNotNull(result);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    @DisplayName("Save shopping cart with valid data")
    void save_ValidCartItemRequestDto_ShouldReturnShoppingCartDto() throws Exception {
        Long bookId = 1L;
        CartItemRequestDto requestDto = CartItemUtil.getCartItemRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        User user = UserUtil.getUser();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        MvcResult result = mockMvc.perform(post("/cart")
                        .with(authentication(auth))
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems[0].bookId").value(bookId))
                .andExpect(jsonPath("$.cartItems[0].bookTitle").value("NewBookTitle1"))
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
