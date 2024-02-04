package adapter.postgresql;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ylab.adapter.repository.postgresql.PostgresAuditRepository;

import java.sql.Connection;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class PostgresAuditRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer
            = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    private static PostgresAuditRepository postgresAuditRepository;

    @BeforeAll
    static void beforeAll() {
        String jdbcUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();
        postgresContainer.start();

        Connection connection = TestUtils.initPostgresConnection(jdbcUrl, username, password);
        postgresAuditRepository = new PostgresAuditRepository(connection);
    }

    @DisplayName("Given username and message, when saveAudit, then add audit to repository")
    @Test
    void givenUsernameAndMessage_saveAudit_shouldAddAuditToRepository() {
        assertDoesNotThrow(() -> {
            assertEquals(1, postgresAuditRepository.getAudit().size());
            postgresAuditRepository.saveAudit("username", "message1");
            postgresAuditRepository.saveAudit("username", "message2");
            postgresAuditRepository.saveAudit("username", "message3");
            assertEquals(4, postgresAuditRepository.getAudit().size());
            assertThat(postgresAuditRepository.getAudit()).containsExactlyElementsOf(
                    new ArrayList<>(java.util.Arrays.asList(
                            "inserts successfully done",
                            "username: message1",
                            "username: message2",
                            "username: message3")
            ));
        });
    }
}
