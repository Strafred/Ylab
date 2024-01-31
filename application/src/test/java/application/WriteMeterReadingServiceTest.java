package application;

import application.port.out.AuditRepository;
import application.port.out.MeterDataRepository;
import application.port.out.MeterTypeRepository;
import application.port.out.UserMetersRepository;
import application.service.meterdata.WriteMeterReadingService;
import application.service.meterdata.exceptions.NoSuchMeterTypeException;
import model.meterdata.MeterType;
import model.user.User;
import model.usermeter.UserMeters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class WriteMeterReadingServiceTest {
    @InjectMocks
    WriteMeterReadingService writeMeterReadingService;

    @Mock
    MeterDataRepository meterDataRepository;
    @Mock
    UserMetersRepository userMetersRepository;
    @Mock
    MeterTypeRepository meterTypeRepository;
    @Mock
    AuditRepository auditRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenNonExistentMeterType_writeMeterReading_shouldThrowException() {
        Mockito.when(meterTypeRepository.findMeterType(new MeterType("123"))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            writeMeterReadingService.writeMeterReading(new MeterType("123"), 123, "user", new User("user", "encodedPassword"));
        }).isInstanceOf(NoSuchMeterTypeException.class);
    }

    @Test
    void givenCorrectInfo_writeMeterReading_shouldWork() {
        Mockito.when(meterTypeRepository.findMeterType(new MeterType("123"))).thenReturn(Optional.of(new MeterType("123")));
        Mockito.when(userMetersRepository.getUserMetersByUsername("user")).thenReturn(new UserMeters(new User("user", "encodedPassword")));

        assertThatCode(() -> {
            writeMeterReadingService.writeMeterReading(new MeterType("123"), 123, "user", new User("user", "encodedPassword"));
        }).doesNotThrowAnyException();

        Mockito.verify(auditRepository, Mockito.times(2)).saveAudit(Mockito.any(), Mockito.any());
        Mockito.verify(meterDataRepository, Mockito.times(1)).putMeterData(Mockito.any());
        Mockito.verify(userMetersRepository, Mockito.times(1)).putUserMeterByUsername(Mockito.any(), Mockito.any());
    }
}
