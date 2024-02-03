package application;

import application.port.in.dto.MeterDataDTO;
import application.port.in.dto.MeterReadingDTO;
import application.port.in.exceptions.AccessDeniedException;
import application.port.repository.*;
import application.service.exceptions.NoSuchMeterTypeException;
import application.service.MeterServiceImpl;
import model.meterdata.*;
import model.user.User;
import model.usermeter.UserMeters;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.util.*;

import static application.service.UserServiceImpl.hashPassword;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MeterServiceImplTest {
    @InjectMocks
    MeterServiceImpl meterServiceImpl;

    @Mock
    Connection connectionMock;

    @Mock
    MeterDataReadingRepository meterDataReadingRepository;

    @Mock
    MeterDataRepository meterDataRepository;
    @Mock
    MeterTypeRepository meterTypeRepository;
    @Mock
    UserMetersRepository userMetersRepository;
    @Mock
    AuditRepository auditRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Give non-existent meter type, when writeMeterReading, then throw NoSuchMeterTypeException")
    @Test
    void givenNonExistentMeterType_writeMeterReading_shouldThrowException() {
        assertDoesNotThrow(() -> {
            Mockito.when(meterTypeRepository.findMeterType(new MeterType("123"))).thenReturn(Optional.empty());
        });
        assertThatThrownBy(() -> {
            meterServiceImpl.writeMeterReading(new MeterType("123"), 123, "user", new User("user", "encodedPassword"));
        }).isInstanceOf(NoSuchMeterTypeException.class);
    }

    @DisplayName("Given correct info, when writeMeterReading, then write meter reading")
    @Test
    void givenCorrectInfo_writeMeterReading_shouldWork() {
        assertDoesNotThrow(() -> {
            Mockito.when(meterTypeRepository.findMeterType(new MeterType("123"))).thenReturn(Optional.of(new MeterType("123")));

            var user = new User("user", "encodedPassword");
            Mockito.when(userMetersRepository.getUserMetersByUsername("user")).thenReturn(new UserMeters(user));

            meterServiceImpl.writeMeterReading(new MeterType("123"), 123, "user", new User("user", "encodedPassword"));

            Mockito.verify(auditRepository, Mockito.times(2)).saveAudit(Mockito.any(), Mockito.any());
            Mockito.verify(meterDataRepository, Mockito.times(1)).putMeterData(Mockito.any(), Mockito.any());
            Mockito.verify(userMetersRepository, Mockito.times(1)).putUserMeterByUsername(Mockito.any(), Mockito.any());
        });
    }

    @Test
    void givenMonthAndYear_getUsersSpecificMonthReadings_shouldReturnCorrectMeterReadings() {
        var user = assertDoesNotThrow(() -> new User("user", "hashedPassword"));

        var meterData1 = new MeterData(new MeterType("Hot water"));
        var meterDataReadings1 = new MeterDataReadings(meterData1);

        var meterData2 = new MeterData(new MeterType("Cold water"));
        var meterDataReadings2 = new MeterDataReadings(meterData2);

        var meterData3 = new MeterData(new MeterType("Gas"));
        var meterDataReadings3 = new MeterDataReadings(meterData3);

        var meterDataReadings1Mock = Mockito.spy(meterDataReadings1);
        Mockito.when(meterDataReadings1Mock.getAllReadings()).thenReturn(Map.of(
                new ReadingDate(2021, 1), new ReadingData(100),
                new ReadingDate(2021, 2), new ReadingData(200),
                new ReadingDate(2021, 3), new ReadingData(300)
        ));

        var meterDataReadings2Mock = Mockito.spy(meterDataReadings2);
        Mockito.when(meterDataReadings2Mock.getAllReadings()).thenReturn(Map.of(
                new ReadingDate(2022, 1), new ReadingData(100),
                new ReadingDate(2022, 2), new ReadingData(200),
                new ReadingDate(2022, 3), new ReadingData(300)
        ));

        var meterDataReadings3Mock = Mockito.spy(meterDataReadings3);
        Mockito.when(meterDataReadings3Mock.getAllReadings()).thenReturn(Map.of(
                new ReadingDate(2021, 1), new ReadingData(700),
                new ReadingDate(2021, 2), new ReadingData(800),
                new ReadingDate(2021, 3), new ReadingData(900)
        ));

        assertDoesNotThrow(() -> {
            Mockito.when(meterDataReadingRepository.getMeterDataReadingsByMeterData(meterData1)).thenReturn(meterDataReadings1Mock);
            Mockito.when(meterDataReadingRepository.getMeterDataReadingsByMeterData(meterData2)).thenReturn(meterDataReadings2Mock);
            Mockito.when(meterDataReadingRepository.getMeterDataReadingsByMeterData(meterData3)).thenReturn(meterDataReadings3Mock);

            var userMetersMock = new UserMeters(user, new ArrayList<>(){{
                add(meterData1);
                add(meterData2);
                add(meterData3);
            }});

            Mockito.when(userMetersRepository.getUserMetersByUsername("user")).thenReturn(userMetersMock);

            List<MeterReadingDTO> returnedMeterReadingDTO = new ArrayList<>();
            returnedMeterReadingDTO.add(new MeterReadingDTO(new MeterType("Hot water"), new ReadingDate(2021, 1), new ReadingData(100)));
            returnedMeterReadingDTO.add(new MeterReadingDTO(new MeterType("Gas"), new ReadingDate(2021, 1), new ReadingData(700)));

            assertThatCode(() -> assertThat(meterServiceImpl.getUsersSpecificMonthReadings(1, 2021, "user", user))
                    .containsExactlyElementsOf(returnedMeterReadingDTO)).doesNotThrowAnyException();
        });
    }

    @Test
    void givenAnotherUserUsername_getUsersSpecificMonthReadings_shouldThrowAccessDeniedException() {
        var user = assertDoesNotThrow(() -> new User("user", "hashedPassword"));

        assertThatThrownBy(() -> meterServiceImpl.getUsersSpecificMonthReadings(1, 2021, "anotherUser", user))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void givenAnotherUserUsername_getMetersHistory_shouldThrowAccessDeniedException() {
        User loggedInUser = assertDoesNotThrow(() -> new User("test", hashPassword("test")));
        String username = "anotherUser";

        assertThatThrownBy(() -> meterServiceImpl.getMetersHistory(username, loggedInUser))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void givenSameUserUsername_getMetersHistory_shouldWork() {
        User loggedInUser = assertDoesNotThrow(() -> new User("test", hashPassword("test")));
        String username = "test";

        UserMeters userMeters = new UserMeters(loggedInUser);
        MeterType meterType = new MeterType("TestMeterType");
        MeterData meterData = new MeterData(meterType);
        userMeters.addMeter(meterData);

        assertDoesNotThrow(() -> {
            Mockito.when(meterDataReadingRepository.getMeterDataReadingsByMeterData(meterData))
                    .thenReturn(new MeterDataReadings(meterData));

            Mockito.when(userMetersRepository.getUserMetersByUsername(username))
                    .thenReturn(userMeters);

            MeterDataDTO returnedMeterDataDTO = new MeterDataDTO(meterType, new HashMap<>());

            assertThatCode(() -> {
                assertThat(meterServiceImpl.getMetersHistory(username, loggedInUser).size()).isEqualTo(1);
                assertThat(meterServiceImpl.getMetersHistory(username, loggedInUser).get(0)).isEqualTo(returnedMeterDataDTO);
            }).doesNotThrowAnyException();
        });
    }

    @Test
    void getAccessibleMeterTypes_shouldWork() {
        assertDoesNotThrow(() -> {
            Mockito.when(meterTypeRepository.getMeterTypes())
                    .thenReturn(new ArrayList<>(List.of(new MeterType("TestMeterType"))));

            List<MeterType> accessibleMeterTypes = meterServiceImpl.getAccessibleMeterTypes();

            AssertionsForClassTypes.assertThat(accessibleMeterTypes.size()).isEqualTo(1);
            AssertionsForClassTypes.assertThat(accessibleMeterTypes.get(0).getMeterTypeName()).isEqualTo("TestMeterType");
        });
    }
}
