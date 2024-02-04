package adapter.postgresql;

import application.port.repository.MeterDataRepository;
import model.meterdata.MeterData;
import model.meterdata.MeterType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ylab.adapter.repository.postgresql.PostgresMeterDataRepository;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Testcontainers
public class PostgresMeterDataRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer
            = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    private static MeterDataRepository meterDataRepository;

    @BeforeAll
    static void beforeAll() {
        String jdbcUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();
        postgresContainer.start();

        Connection connection = TestUtils.initPostgresConnection(jdbcUrl, username, password);
        meterDataRepository = new PostgresMeterDataRepository(connection);
    }

    @DisplayName("Given meter data, when postMeterData, then save meter data")
    @Test
    @Order(1)
    void givenMeterData_postMeterData_shouldSaveMeterData() {
        var meterType = new MeterType(3, "Gas");
        var meterData = new MeterData(meterType);

        assertDoesNotThrow(() -> {
            assertThat(meterDataRepository.getMeterData().size()).isEqualTo(5);
            var meterDataPosted = meterDataRepository.postMeterData(meterData, "user");
            meterDataRepository.getMeterData().contains(meterDataPosted);
        });
    }

    @DisplayName("When getMeterData, then return meter data")
    @Test
    @Order(2)
    void getMeterData_shouldReturnMeterData() {
        assertDoesNotThrow(() -> {
            assertThat(meterDataRepository.getMeterData().size()).isEqualTo(6);
        });
    }
}
