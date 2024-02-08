package adapter.postgresql;

import model.user.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ylab.adapter.repository.postgresql.PostgresUserRepository;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class PostgresUserRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer
            = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    private static PostgresUserRepository postgresUserRepository;

    @BeforeAll
    static void beforeAll() {
        String jdbcUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();
        postgresContainer.start();

        Connection connection = TestUtils.initPostgresConnection(jdbcUrl, username, password);
        postgresUserRepository = new PostgresUserRepository(connection);
    }

    @DisplayName("Given user, when postUser, then save user correctly")
    @Test
    void givenUser_postUser_shouldSaveUserCorrectly() {
        assertDoesNotThrow(() -> {
            var user = new User("testUser", "encodedPassword");
            user = postgresUserRepository.postUser(user);

            assertThat(postgresUserRepository.getUser("testUser")).isEqualTo(user);
        });
    }

    @DisplayName("Given existing user, when userExists, then return true")
    @Test
    void givenExistingUser_userExists_shouldReturnTrue() {
        assertDoesNotThrow(() -> {
            var user = new User("testUser2", "encodedPassword");
            user = postgresUserRepository.postUser(user);

            assertThat(postgresUserRepository.userExists("testUser2")).isTrue();
        });
    }

    @DisplayName("Given non-existing user, when userExists, then return false")
    @Test
    void givenNonExistingUser_userExists_shouldReturnFalse() {
        assertDoesNotThrow(() -> {
            assertThat(postgresUserRepository.userExists("nonExistingUser")).isFalse();
        });
    }

    @DisplayName("When getUser, then return user")
    @Test
    void getUser_shouldReturnUser() {
        assertDoesNotThrow(() -> {
            var user = new User("testUser3", "encodedPassword");
            user = postgresUserRepository.postUser(user);

            assertThat(postgresUserRepository.getUser("testUser3")).isEqualTo(user);
        });
    }
}
