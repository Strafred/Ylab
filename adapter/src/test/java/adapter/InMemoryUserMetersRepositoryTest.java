package adapter;

import model.meterdata.MeterData;
import model.meterdata.MeterType;
import model.user.User;
import model.usermeter.UserMeters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ylab.adapter.repository.inmemory.InMemoryUserMetersRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class InMemoryUserMetersRepositoryTest {
    @DisplayName("Given user meters, when saveUserMeters, then save user meters")
    @Test
    void givenUserMeters_saveUserMeters_shouldSaveUserMeters() {
        InMemoryUserMetersRepository inMemoryUserMetersRepository = new InMemoryUserMetersRepository();

        var user = assertDoesNotThrow(() -> new User("username", "password"));
        inMemoryUserMetersRepository.saveUserMeters(new UserMeters(user));
        assertThat(inMemoryUserMetersRepository.getUserMetersByUsername("username")).isNotNull();
    }

    @DisplayName("Given username and meter data, when putUserMeterByUsername, then put meter data")
    @Test
    void givenMeterData_putUserMeterByUsername_shouldPutMeterData() {
        InMemoryUserMetersRepository inMemoryUserMetersRepository = new InMemoryUserMetersRepository();

        var user = assertDoesNotThrow(() -> new User("username", "password"));
        inMemoryUserMetersRepository.saveUserMeters(new UserMeters(user));
        var meterData = new MeterData(new MeterType("123"));
        inMemoryUserMetersRepository.putUserMeterByUsername("username", meterData);

        assertThat(inMemoryUserMetersRepository.getUserMetersByUsername("username").getMeters().size()).isEqualTo(1);
        assertThat(inMemoryUserMetersRepository.getUserMetersByUsername("username").getMeters().get(0)).isEqualTo(meterData);
    }
}
