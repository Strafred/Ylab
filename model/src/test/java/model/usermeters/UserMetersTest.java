package model.usermeters;

import model.meterdata.MeterData;
import model.meterdata.MeterType;
import model.user.User;
import model.usermeter.UserMeters;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserMetersTest {
    @Test
    void givenAMeterData_addMeter_shouldWork() {
        UserMeters userMeters = new UserMeters(new User("test", "test"));
        userMeters.addMeter(new MeterData(new MeterType("TestMeterType")));

        assertThat(userMeters.getMeters().size()).isEqualTo(1);
    }
}
