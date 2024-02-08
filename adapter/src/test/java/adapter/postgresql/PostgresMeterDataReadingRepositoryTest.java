package adapter.postgresql;

import model.meterdata.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ylab.adapter.repository.postgresql.PostgresMeterDataReadingRepository;

import java.sql.Connection;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Testcontainers
public class PostgresMeterDataReadingRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer
            = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    private static PostgresMeterDataReadingRepository postgresMeterDataReadingRepository;

    @BeforeAll
    static void beforeAll() {
        String jdbcUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();
        postgresContainer.start();

        Connection connection = TestUtils.initPostgresConnection(jdbcUrl, username, password);
        postgresMeterDataReadingRepository = new PostgresMeterDataReadingRepository(connection);
    }

    @DisplayName("Given meter data readings, when putMeterDataReadings, then add meter data readings to repository")
    @Test
    @Order(1)
    void givenMeterDataReadings_putMeterDataReadings_shouldAddMeterDataReadingsToRepository() {
        var meterType = new MeterType(1, "Cold water");
        var meterData = new MeterData(1, meterType);
        assertDoesNotThrow(() -> {
            MeterDataReadings meterDataReadings = new MeterDataReadings(meterData);
            meterDataReadings.addReading(new ReadingData(42));
            postgresMeterDataReadingRepository.putMeterDataReadings(meterDataReadings);

            assertThat(postgresMeterDataReadingRepository
                    .getMeterDataReadingsByMeterData(meterData)
                    .getAllReadings()
                    .get(new ReadingDate(YearMonth.now().getYear(), YearMonth.now().getMonthValue())))
                    .isEqualTo(new ReadingData(42));
        });
    }

    @DisplayName("Given meter data, when getMeterDataReadingsByMeterData, then return meter data readings")
    @Test
    @Order(2)
    void givenMeterData_getMeterDataReadingsByMeterData_shouldReturnMeterDataReadings() {
        var meterType = new MeterType(1, "Cold water");
        var meterData = new MeterData(1, meterType);
        assertDoesNotThrow(() -> {
            var meterDataReadings = postgresMeterDataReadingRepository.getMeterDataReadingsByMeterData(meterData);

            assertThat(meterDataReadings.getAllReadings().get(new ReadingDate(YearMonth.now().getYear(), YearMonth.now().getMonthValue())))
                    .isEqualTo(new ReadingData(42));
        });
    }

    @DisplayName("Given meter data and reading, when putNewReadingByMeterData, then add new reading to repository")
    @Test
    @Order(3)
    void givenMeterDataAndReading_putNewReadingByMeterData_shouldAddNewReadingToRepository() {
        var meterType = new MeterType(2, "Hot water");
        var meterData = new MeterData(2, meterType);
        assertDoesNotThrow(() -> {
            postgresMeterDataReadingRepository.putNewReadingByMeterData(meterData, new ReadingData(43));

            assertThat(postgresMeterDataReadingRepository
                    .getMeterDataReadingsByMeterData(meterData)
                    .getAllReadings()
                    .get(new ReadingDate(YearMonth.now().getYear(), YearMonth.now().getMonthValue())))
                    .isEqualTo(new ReadingData(43));
        });
    }
}
