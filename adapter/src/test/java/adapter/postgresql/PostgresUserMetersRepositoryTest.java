package adapter.postgresql;

import model.meterdata.MeterData;
import model.meterdata.MeterType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ylab.adapter.repository.postgresql.PostgresUserMetersRepository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Testcontainers
public class PostgresUserMetersRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer
            = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    private static PostgresUserMetersRepository postgresUserMetersRepository;

    @BeforeAll
    static void beforeAll() {
        String jdbcUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();
        postgresContainer.start();

        Connection connection = TestUtils.initPostgresConnection(jdbcUrl, username, password);
        postgresUserMetersRepository = new PostgresUserMetersRepository(connection);
    }

    @DisplayName("Given username, when getUserMetersByUsername, then return user meters")
    @Test
    void getUserMetersByUsername_ShouldReturnUserMeters() {
        assertDoesNotThrow(() -> {
            var userMeters = postgresUserMetersRepository.getUserMetersByUsername("strafred");

            var meterType1 = new MeterType(1, "Cold water");
            var meterData1 = new MeterData(1, meterType1);

            var meterType2 = new MeterType(2, "Hot water");
            var meterData2 = new MeterData(2, meterType2);

            var meterType3 = new MeterType(3, "Gas");
            var meterData3 = new MeterData(3, meterType3);

            List<MeterData> meterDataList = new ArrayList<>();
            meterDataList.add(meterData1);
            meterDataList.add(meterData2);
            meterDataList.add(meterData3);

            assertThat(userMeters.getMeters())
                    .containsExactlyElementsOf(meterDataList);
        });
    }
}
