package com.example.bookstoreapp.repository;

import com.example.bookstoreapp.repository.category.CategoryRepository;
import java.sql.Connection;
import java.sql.SQLException;
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
import org.springframework.jdbc.datasource.init.ScriptUtils;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {
    private static final String INSERT_CATEGORIES_SCRIPT_PATH =
            "database/categories/insert-categories-to-test-db.sql";

    private static final String DELETE_ALL_SCRIPT_PATH = "database/delete-all-data.sql";

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Should return true when category exists by ID")
    public void existById_CategoryExist_ReturnTrue() {
        Long existingCategoryId = 1L;

        boolean exists = categoryRepository.existsById(existingCategoryId);

        Assertions.assertTrue(exists, "Category with ID " + existingCategoryId
                + " should exist in the database.");
    }

    @Test
    @DisplayName("Should return false when category does not exist by ID")
    void existsById_CategoryDoesNotExist_ShouldReturnFalse() {
        Long nonExistingCategoryId = 999L;

        boolean exists = categoryRepository.existsById(nonExistingCategoryId);

        Assertions.assertFalse(exists, "Category with ID " + nonExistingCategoryId
                + " should not exist in the database.");
    }

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource) throws SQLException {
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(INSERT_CATEGORIES_SCRIPT_PATH));
        }
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
