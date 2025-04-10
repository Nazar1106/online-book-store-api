package com.example.bookstoreapp.controller;

import static com.example.bookstoreapp.testutil.CategoryUtil.createBooksList;
import static com.example.bookstoreapp.testutil.CategoryUtil.createCategoryRequestDto;
import static com.example.bookstoreapp.testutil.CategoryUtil.expectedCategoryResponseDto;
import static com.example.bookstoreapp.testutil.CategoryUtil.expectedNewCategory;
import static com.example.bookstoreapp.testutil.CategoryUtil.updateCategory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstoreapp.dto.categorydto.BookDtoWithoutCategoryIds;
import com.example.bookstoreapp.dto.categorydto.CategoryRequestDto;
import com.example.bookstoreapp.dto.categorydto.CategoryResponseDto;
import com.example.bookstoreapp.testutil.CategoryUtil;
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
class CategoryControllerTest {

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
                    new ClassPathResource(INSERT_CATEGORIES_SCRIPT_PATH));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_BOOKS_SCRIPT_PATH));
        }
    }

    @Test
    @DisplayName("Create new valid category")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void createCategory_ValidData_ShouldReturnCategoryResponseDto() throws Exception {
        CategoryRequestDto requestDto =
                createCategoryRequestDto();

        CategoryResponseDto expectedDto = expectedNewCategory();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryResponseDto responseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class);

        assertThat(expectedDto).usingRecursiveComparison().isEqualTo(responseDto);
    }

    @Test
    @DisplayName("Retrieve existing categories")
    @WithMockUser(username = "user", authorities = "USER")
    void getAll_ExistingData_ShouldReturnPageCategoryResponseDto() throws Exception {
        Pageable pageable = Pageable.ofSize(4);
        List<CategoryResponseDto> expectedCategories = CategoryUtil.getAllCategories();

        String jsonRequest = objectMapper.writeValueAsString(pageable);

        MvcResult result = mockMvc.perform(get("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());

        List<CategoryResponseDto> actual = objectMapper.readValue(root.get("content").toString(),
                new TypeReference<>(){});

        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedCategories);
    }

    @Test
    @DisplayName("Update category by id")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void updateCategory_ValidData_ShouldReturnUpdatedCategoryDto() throws Exception {
        Long categoryId = 1L;
        String updatedName = "UpdatedName";
        String updatedDescription = "UpdatedDescription";

        CategoryResponseDto updateRequestDto =
                updateCategory(categoryId, updatedName, updatedDescription);

        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        MvcResult result = mockMvc.perform(put("/categories/{id}", updateRequestDto.getId())
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto resultDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class);

        assertThat(updateRequestDto).usingRecursiveComparison().isEqualTo(resultDto);
    }

    @Test
    @DisplayName("Update category by id with out authorities")
    @WithMockUser(username = "user", authorities = {"USER"})
    void updateCategory_WithoutPermission_ShouldReturnForbidden() throws Exception {
        Long categoryId = 1L;

        CategoryRequestDto requestDto = createCategoryRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/categories/{id}", categoryId)
                        .content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Update category with non-existent ID should return Not Found")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void updateCategory_NonExistentCategory_ShouldReturnNotFound() throws Exception {
        Long categoryId = 999L;

        CategoryRequestDto updateRequestDto =
                createCategoryRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        mockMvc.perform(put("/categories/{id}", categoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get category by valid id")
    @WithMockUser(username = "user", authorities = {"USER"})
    void getCategoryById_ValidId_ShouldReturnCategoryResponseDto() throws Exception {
        CategoryResponseDto expectedDto = expectedCategoryResponseDto();

        MvcResult result = mockMvc.perform(get("/categories/{id}", expectedDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto resultDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class);

        verifyCategoryResponseDto(expectedDto, resultDto);
    }

    @Test
    @DisplayName("Get books by category ID - Success")
    @WithMockUser(username = "user", authorities = {"USER"})
    void getBooksByCategoryId_ValidCategoryId_ShouldReturnBooks() throws Exception {
        Long categoryId = 1L;
        List<BookDtoWithoutCategoryIds> expectedBooks = createBooksList();

        MvcResult result = mockMvc.perform(get("/categories/{id}/books", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDtoWithoutCategoryIds> actualBooks = objectMapper
                .readValue(result.getResponse()
                        .getContentAsString(), objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, BookDtoWithoutCategoryIds.class));

        assertThat(actualBooks).usingRecursiveComparison().isEqualTo(expectedBooks);
    }

    @Test
    @DisplayName("Get books by category ID - Unauthorized access")
    void getBooksByCategoryId_UnauthorizedAccess_ShouldReturnUnauthorized() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(get("/categories/{id}/books", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Delete category by id")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void deleteCategory_ById_ShouldReturnNoContent() throws Exception {
        Long categoryIdToDelete = 1L;

        mockMvc.perform(delete("/categories/{id}", categoryIdToDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @DisplayName("Delete category by id with out authorities")
    @WithMockUser(username = "user", authorities = {"USER"})
    void deleteCategory_WithoutPermission_ShouldReturnForbidden() throws Exception {
        Long categoryIdToDelete = 1L;

        mockMvc.perform(delete("/categories/{id}", categoryIdToDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    private void verifyCategoryResponseDto(CategoryResponseDto expected,
                                           CategoryResponseDto actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
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
                    new ClassPathResource(REMOVE_ALL_SCRIPT_PATH));
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }
}
