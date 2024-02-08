package adapter.postgresql;

import model.meterdata.MeterType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ylab.adapter.repository.postgresql.PostgresMeterTypeRepository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class PostgresMeterTypeRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer
            = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    private static PostgresMeterTypeRepository postgresMeterTypeRepository;

    @BeforeAll
    static void beforeAll() {
        String jdbcUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();
        postgresContainer.start();

        Connection connection = TestUtils.initPostgresConnection(jdbcUrl, username, password);
        postgresMeterTypeRepository = new PostgresMeterTypeRepository(connection);
    }

    @DisplayName("Given meter type, when addMeterType, then add meter type")
    @Test
    @Order(1)
    void givenMeterType_addMeterType_shouldAddMeterType() {
        assertDoesNotThrow(() -> {
            var meterType = new MeterType("Test meter type");
            postgresMeterTypeRepository.addMeterType(meterType);
            assertEquals(4, postgresMeterTypeRepository.getMeterTypes().size());
            assertThat(postgresMeterTypeRepository.getMeterTypes()).contains(meterType);
        });
    }

    @DisplayName("When getMeterTypes, then return allowed meter types")
    @Test
    @Order(2)
    void getMeterTypes_shouldReturnAllowedMeterTypes() {
        List<MeterType> starterMeterTypes = new ArrayList<>();
        starterMeterTypes.add(new MeterType("Cold water"));
        starterMeterTypes.add(new MeterType("Hot water"));
        starterMeterTypes.add(new MeterType("Gas"));
        starterMeterTypes.add(new MeterType("Test meter type"));

        assertDoesNotThrow(() -> {
            assertEquals(4, postgresMeterTypeRepository.getMeterTypes().size());
            assertThat(postgresMeterTypeRepository.getMeterTypes()).containsExactlyElementsOf(starterMeterTypes);
        });
    }

    @DisplayName("Given meter type, when findMeterType, then return meter type")
    @Test
    @Order(3)
    void givenMeterType_findMeterType_shouldReturnMeterType() {
        assertDoesNotThrow(() -> {
            var meterType = new MeterType(1, "Cold water");
            assertThat(postgresMeterTypeRepository.findMeterType(meterType).get()).isEqualTo(meterType);
        });
    }
}
