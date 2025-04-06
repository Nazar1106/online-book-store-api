package com.example.bookstoreapp.controller;

import static com.example.bookstoreapp.testutil.BookUtil.creatBookRequestDto;
import static com.example.bookstoreapp.testutil.BookUtil.createExpectedBookDto;
import static com.example.bookstoreapp.testutil.BookUtil.getBookDto;
import static com.example.bookstoreapp.testutil.BookUtil.getInvalidCreateBookRequestDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstoreapp.dto.bookdto.BookDto;
import com.example.bookstoreapp.dto.bookdto.BookSearchParametersDto;
import com.example.bookstoreapp.dto.bookdto.CreateBookRequestDto;
import com.example.bookstoreapp.testutil.BookUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    private static final String INSERT_BOOKS_SCRIPT_PATH =
            "database/books/insert-books-with-categories-to-db.sql";
    private static final String INSERT_CATEGORIES_SCRIPT_PATH =
            "database/categories/insert-categories-to-test-db.sql";
    private static final String REMOVE_ALL_SCRIPT_PATH =
            "database/delete-all-data.sql";
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext,
                          @Autowired DataSource dataSource) {
        teardown(dataSource);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws SQLException {
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_CATEGORIES_SCRIPT_PATH));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_BOOKS_SCRIPT_PATH));
        }
    }

    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    @DisplayName("Get all books by existing data, should return a page of BookDto")
    void getAll_ByExistData_ShouldReturnPageBookDto() throws Exception {
        Pageable requestDto = Pageable.ofSize(3);
        List<BookDto> expectedBooks = BookUtil.getAllBooks();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "3")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<BookDto> actualBooks = objectMapper.readValue(
                root.get("content").toString(),
                new TypeReference<>() {
                }
        );
        assertThat(actualBooks).usingRecursiveComparison().isEqualTo(expectedBooks);
    }

    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    void search_ExistData_ShouldReturnListBookDto() throws Exception {
        BookSearchParametersDto searchRequest = new BookSearchParametersDto(
                new String[]{"NewBookAuthor1"}, null, null, null);

        List<BookDto> expected = BookUtil.searchBooksByParams();

        String jsonRequest = objectMapper.writeValueAsString(searchRequest);

        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("author", "NewBookAuthor1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        assertThat(expected).usingRecursiveComparison().isEqualTo(actual);
    }

    @Test
    @DisplayName("Create book with valid data")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void createBook_ValidData_ShouldReturnCreatedBookDto() throws Exception {
        Long testId = 4L;

        BookDto expectedDto = createExpectedBookDto(testId);
        CreateBookRequestDto requestDto = creatBookRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto resultDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        verifyBookDtoEquality(expectedDto, resultDto);
    }

    @Test
    @DisplayName("Create book with invalid data")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void createBook_NotValidData_ShouldReturnException() throws Exception {
        CreateBookRequestDto requestDto = getInvalidCreateBookRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertTrue(responseContent.contains("price must be greater than 0"),
                "Expected validation message for price");
    }

    @Test
    @DisplayName("Update book with valid data")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void updateBook_ValidData_ShouldReturnUpdatedBookDto() throws Exception {
        Long testId = 1L;

        BookDto expectedDto = createExpectedBookDto(
                testId);
        CreateBookRequestDto requestDto = creatBookRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/books/{id}", testId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto resultDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        verifyBookDtoEquality(expectedDto, resultDto);
    }

    @Test
    @DisplayName("Update book with invalid ID")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void updateBook_InvalidId_ShouldReturnNotFound() throws Exception {
        Long invalidId = 999L;

        CreateBookRequestDto requestDto = creatBookRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/books/{id}", invalidId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();

        String responseContent = result.getResponse().getContentAsString();

        assertTrue(responseContent.contains("Can't update book by id " + invalidId));
    }

    @Test
    @DisplayName("Delete book by valid id")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void deleteBook_ById_ShouldReturnNoContent() throws Exception {
        Long testId = 1L;

        mockMvc.perform(delete("/books/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @DisplayName("Delete book with invalid ID")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void deleteBook_InvalidId_ShouldReturnNotFound() throws Exception {
        Long invalidId = 999L;

        mockMvc.perform(delete("/books/{id}", invalidId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get book by valid id")
    @WithMockUser(username = "user", authorities = {"USER"})
    void getBook_ByValidId_ShouldReturnBookDto() throws Exception {
        Long testId = 1L;
        BookDto expectedDto = getBookDto(testId);

        MvcResult result = mockMvc.perform(get("/books/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto resultDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        verifyBookDtoEquality(expectedDto, resultDto);
    }

    @Test
    @DisplayName("Get book with invalid ID")
    @WithMockUser(username = "user", authorities = {"USER"})
    void getBook_ByInvalidId_ShouldReturnNotFound() throws Exception {
        Long invalidId = 999L;

        mockMvc.perform(get("/books/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private void verifyBookDtoEquality(BookDto expected, BookDto actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getIsbn(), actual.getIsbn());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getCoverImage(), actual.getCoverImage());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getCategoryIds(), actual.getCategoryIds());
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(REMOVE_ALL_SCRIPT_PATH));
        }
    }
}
