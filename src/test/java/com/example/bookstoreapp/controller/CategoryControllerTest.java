package com.example.bookstoreapp.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstoreapp.dto.categorydto.BookDtoWithoutCategoryIds;
import com.example.bookstoreapp.dto.categorydto.CategoryRequestDto;
import com.example.bookstoreapp.dto.categorydto.CategoryResponseDto;
import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.exception.EntityNotFoundException;
import com.example.bookstoreapp.repository.book.BookRepository;
import com.example.bookstoreapp.service.impl.CategoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    @Mock
    private CategoryServiceImpl categoryService;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private CategoryController categoryController;

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
    @DisplayName("Create new category")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void createCategory_ValidData_ShouldReturnCategoryDto() throws Exception {
        String testName = "TestName";
        String testDescription = "TestDescription";

        CategoryRequestDto expectedDto = createCategoryRequestDtoDto(testName, testDescription);

        CategoryRequestDto requestDto =
                createCategoryRequestDtoDto(testName, testDescription);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryRequestDto resultDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryRequestDto.class);

        assertNotNull(resultDto);
        assertEquals(expectedDto.getName(), resultDto.getName());
        assertEquals(expectedDto.getDescription(), resultDto.getDescription());
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

        assertNotNull(resultDto);
        assertEquals(categoryId, resultDto.getId());
        assertEquals(updatedName, resultDto.getName());
        assertEquals(updatedDescription, resultDto.getDescription());
    }

    @Test
    @DisplayName("Update category by id with out authorities")
    @WithMockUser(username = "user", authorities = {"USER"})
    void updateCategory_WithoutPermission_ShouldReturnForbidden() throws Exception {
        Long categoryId = 1L;
        String updatedName = "UpdatedName";
        String updatedDescription = "UpdatedDescription";

        CategoryRequestDto updateRequestDto =
                createCategoryRequestDtoDto(updatedName, updatedDescription);

        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        mockMvc.perform(put("/categories/{id}", categoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @DisplayName("Update category with non-existent ID should return Not Found")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void updateCategory_NonExistentCategory_ShouldReturnNotFound() throws Exception {
        Long categoryId = 999L;
        String updatedName = "UpdatedCategoryName";
        String updatedDescription = "UpdatedCategoryDescription";

        CategoryRequestDto updateRequestDto =
                createCategoryRequestDtoDto(updatedName, updatedDescription);
        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        when(categoryService.update(eq(categoryId), any(CategoryRequestDto.class)))
                .thenThrow(new EntityNotFoundException("can't find Category by id " + categoryId));

        mockMvc.perform(put("/categories/{id}", categoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("can't find Category by id " + categoryId))
                .andReturn();
    }

    @Test
    @DisplayName("Get category by valid id")
    @WithMockUser(username = "user", authorities = {"USER"})
    void getCategoryById_ValidId_ShouldReturnCategoryResponseDto() throws Exception {
        Long testId = 1L;
        CategoryResponseDto expectedDto = updateCategory(testId, "Fiction",
                "Books that contain fictional stories");

        MvcResult result = mockMvc.perform(get("/categories/{id}", testId)
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

        when(bookRepository.findByCategoriesId(categoryId))
                .thenReturn(expectedBooks.stream()
                        .map(bookDto -> {
                            Book book = new Book();
                            book.setTitle(bookDto.getTitle());
                            book.setAuthor(bookDto.getAuthor());
                            book.setIsbn(bookDto.getIsbn());
                            book.setPrice(bookDto.getPrice());
                            book.setDescription(bookDto.getDescription());
                            book.setCoverImage(bookDto.getCoverImage());
                            return book;
                        })
                        .collect(Collectors.toList()));

        MvcResult result = mockMvc.perform(get("/categories/{id}/books", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDtoWithoutCategoryIds> resultBooks = objectMapper
                .readValue(result.getResponse()
                                .getContentAsString(), objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, BookDtoWithoutCategoryIds.class));

        assertNotNull(resultBooks);
        assertEquals(expectedBooks.size(), resultBooks.size());
        assertEquals(expectedBooks.get(0).getTitle(), resultBooks.get(0).getTitle());
        assertEquals(expectedBooks.get(1).getTitle(), resultBooks.get(1).getTitle());
    }

    @Test
    @DisplayName("Get books by category ID - Unauthorized access")
    void getBooksByCategoryId_UnauthorizedAccess_ShouldReturnUnauthorized() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(get("/categories/{id}/books", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @DisplayName("Get all categories with pagination - should return correct category page")
    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    public void getAllCategories_WithPagination_ShouldReturnCategoryPage() {

        Pageable pageable = PageRequest.of(0, 10);
        CategoryResponseDto category1 = createCategories().getFirst();
        CategoryResponseDto category2 = createCategories().getLast();

        Page<CategoryResponseDto> expectedPage =
                new PageImpl<>(Arrays.asList(category1, category2), pageable, 2);

        when(categoryService.findAll(pageable)).thenReturn(expectedPage);

        Page<CategoryResponseDto> actualPage = categoryController.getAll(pageable);

        assertThat(actualPage).isNotNull();

        assertThat(actualPage.getContent().get(0)).isInstanceOf(CategoryResponseDto.class);
        assertThat(actualPage.getContent().get(1)).isInstanceOf(CategoryResponseDto.class);

        assertThat(actualPage.getContent().get(0).getName()).isEqualTo("TestCategory 1");
        assertThat(actualPage.getContent().get(1).getName()).isEqualTo("TestCategory 2");

        verify(categoryService, times(1)).findAll(pageable);
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

    private List<CategoryResponseDto> createCategories() {
        CategoryResponseDto category1 = new CategoryResponseDto();
        category1.setId(1L);
        category1.setName("TestCategory 1");
        category1.setDescription("TestDescription 1");

        CategoryResponseDto category2 = new CategoryResponseDto();
        category2.setId(2L);
        category2.setName("TestCategory 2");
        category2.setDescription("TestDescription 2");

        return Arrays.asList(category1, category2);
    }

    private List<BookDtoWithoutCategoryIds> createBooksList() {
        BookDtoWithoutCategoryIds book1 = new BookDtoWithoutCategoryIds();
        book1.setTitle("NewBookTitle1");
        book1.setAuthor("NewBookAuthor1");
        book1.setIsbn("NewBookIsbn1");
        book1.setPrice(BigDecimal.valueOf(150));
        book1.setDescription("NewBookDescription1");
        book1.setCoverImage("NewCoverImage1");

        BookDtoWithoutCategoryIds book2 = new BookDtoWithoutCategoryIds();
        book2.setTitle("NewBookTitle2");
        book2.setAuthor("NewBookAuthor2");
        book2.setIsbn("NewBookIsbn2");
        book2.setPrice(BigDecimal.valueOf(120));
        book2.setDescription("NewBookDescription2");
        book2.setCoverImage("NewCoverImage2");

        return Arrays.asList(book1, book2);
    }

    private CategoryResponseDto updateCategory(Long id, String name, String description) {
        CategoryResponseDto categoryDto = new CategoryResponseDto();
        categoryDto.setId(id);
        categoryDto.setName(name);
        categoryDto.setDescription(description);
        return categoryDto;
    }

    private CategoryRequestDto createCategoryRequestDtoDto(String name, String description) {
        CategoryRequestDto categoryRequestDtoDto = new CategoryRequestDto();
        categoryRequestDtoDto.setName(name);
        categoryRequestDtoDto.setDescription(description);
        return categoryRequestDtoDto;
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
