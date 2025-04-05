package com.example.bookstoreapp.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.bookstoreapp.entity.User;
import com.example.bookstoreapp.repository.user.UserRepository;
import com.example.bookstoreapp.testutil.UserUtil;
import java.sql.Connection;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    private static final String DELETE_ALL_DATA_SQL = "database/delete-all-data.sql";

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName(value = "Return true when user exists")
    public void existsByEmail_ExistUser_ShouldReturnTrue() {
        String userEmail = "john.doe@example.com";

        boolean output = userRepository.existsByEmail(userEmail);

        assertTrue(output);
    }

    @Test
    @DisplayName(value = "Return false when user doesn't exists")
    public void existsByEmail_NoExistUser_ShouldReturnFalse() {
        String userEmail = "noExist.userEmail@gmail.com";

        boolean output = userRepository.existsByEmail(userEmail);

        assertFalse(output);
    }

    @Test
    @DisplayName(value = "Return optional user when user exists")
    public void findByEmail_ExistUser_ShouldReturnOptionalUser() {
        String userEmail = "john.doe@example.com";
        User expectedUser = UserUtil.getUserWithHashedPassword();

        Optional<User> optionalUser = userRepository.findByEmail(userEmail);

        assertTrue(optionalUser.isPresent());

        User actualUser = optionalUser.get();

        assertThat(actualUser).usingRecursiveComparison().isEqualTo(expectedUser);
    }

    @Test
    @DisplayName(value = "Return empty when user doesn't exist")
    public void findByEmail_NoExistUser_ShouldReturnEmpty() {
        String userEmail = "noExist.userEmail@gmail.com";

        Optional<User> optionalUser = userRepository.findByEmail(userEmail);

        assertTrue(optionalUser.isEmpty());
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
