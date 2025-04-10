package com.example.bookstoreapp.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.repository.book.BookRepository;
import com.example.bookstoreapp.testutil.BookUtil;
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
import org.springframework.jdbc.datasource.init.ScriptUtils;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    private static final String INSERT_BOOKS_SCRIPT_PATH =
            "database/books/insert-books-with-categories-to-db.sql";
    private static final String INSERT_CATEGORIES_SCRIPT_PATH =
            "database/categories/insert-categories-to-test-db.sql";
    private static final String DELETE_ALL_SCRIPT_PATH = "database/delete-all-data.sql";

    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource) throws SQLException {
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
    @DisplayName("Should return true when book exists by ID")
    public void existById_BookExist_ReturnTrue() {
        Long existingBookId = 1L;

        boolean exists = bookRepository.existsById(existingBookId);

        assertTrue(exists, "Book with ID " + existingBookId + " should exist in the database.");
    }

    @Test
    @DisplayName("Should return false when book does not exist by ID")
    void existsById_BookDoesNotExist_ShouldReturnFalse() {
        Long nonExistingBookId = 999L;

        boolean exists = bookRepository.existsById(nonExistingBookId);

        assertFalse(exists, "Book with ID " + nonExistingBookId
                + " should not exist in the database.");
    }

    @Test
    @DisplayName("Should return books when categoryId exists")
    void findByCategoriesId_CategoryExists_ShouldReturnBooks() {
        Long existingCategoryId = 1L;
        List<Book> expectedBooks = BookUtil.getExpectedBooks();

        List<Book> actual = bookRepository.findByCategoriesId(existingCategoryId);

        assertNotNull(actual, "Result list should not be null.");
        assertThat(expectedBooks).usingRecursiveComparison().isEqualTo(actual);
    }

    @Test
    @DisplayName("Should return empty list when categoryId does not exist")
    void findByCategoriesId_CategoryDoesNotExist_ShouldReturnEmptyList() {
        Long nonExistingCategoryId = 999L;

        List<Book> books = bookRepository.findByCategoriesId(nonExistingCategoryId);

        assertNotNull(books, "Result list should not be null.");
        assertTrue(books.isEmpty(), "Books list should be empty for non-existing category.");
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
                    new ClassPathResource(DELETE_ALL_SCRIPT_PATH));
        }
    }
}
