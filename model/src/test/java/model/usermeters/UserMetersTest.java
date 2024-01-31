package model.usermeters;

import model.meterdata.MeterData;
import model.meterdata.MeterType;
import model.user.User;
import model.usermeter.UserMeters;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UserMetersTest {
    @Test
    void givenAMeterData_addMeter_shouldWork() {
        User user = assertDoesNotThrow(() -> new User("test", "test"));
        UserMeters userMeters = new UserMeters(user);
        userMeters.addMeter(new MeterData(new MeterType("TestMeterType")));

        assertThat(userMeters.getMeters().size()).isEqualTo(1);
    }
}
