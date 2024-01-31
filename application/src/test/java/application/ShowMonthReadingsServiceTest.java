package application;

import application.port.in.meterdata.dto.MeterReadingDTO;
import application.port.in.meterdata.exceptions.AccessDeniedException;
import application.port.out.AuditRepository;
import application.port.out.UserMetersRepository;
import application.service.meterdata.ShowMonthReadingsService;
import model.meterdata.MeterData;
import model.meterdata.MeterType;
import model.meterdata.ReadingData;
import model.meterdata.ReadingDate;
import model.user.User;
import model.usermeter.UserMeters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ShowMonthReadingsServiceTest {
    @InjectMocks
    ShowMonthReadingsService showMonthReadingsService;

    @Mock
    UserMetersRepository userMetersRepository;

    @Mock
    AuditRepository auditRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenMonthAndYear_getUsersSpecificMonthReadings_shouldReturnCorrectMeterReadings() {
        var user = assertDoesNotThrow(() -> new User("user", "hashedPassword"));

        var meterData1Mock = Mockito.spy(new MeterData(new MeterType("Hot water")));
        Mockito.when(meterData1Mock.getAllReadings()).thenReturn(Map.of(
                new ReadingDate(2021, 1), new ReadingData(100),
                new ReadingDate(2021, 2), new ReadingData(200),
                new ReadingDate(2021, 3), new ReadingData(300)
        ));

        var meterData2Mock = Mockito.spy(new MeterData(new MeterType("Cold water")));
        Mockito.when(meterData2Mock.getAllReadings()).thenReturn(Map.of(
                new ReadingDate(2022, 1), new ReadingData(100),
                new ReadingDate(2022, 2), new ReadingData(200),
                new ReadingDate(2022, 3), new ReadingData(300)
        ));

        var meterData3Mock = Mockito.spy(new MeterData(new MeterType("Gas")));
        Mockito.when(meterData3Mock.getAllReadings()).thenReturn(Map.of(
                new ReadingDate(2021, 1), new ReadingData(700),
                new ReadingDate(2021, 2), new ReadingData(800),
                new ReadingDate(2021, 3), new ReadingData(900)
        ));

        var userMetersMock = new UserMeters(user, new ArrayList<>(){{
            add(meterData1Mock);
            add(meterData2Mock);
            add(meterData3Mock);
        }});

        Mockito.when(userMetersRepository.getUserMetersByUsername("user")).thenReturn(userMetersMock);

        List<MeterReadingDTO> returnedMeterReadingDTO = new ArrayList<>();
        returnedMeterReadingDTO.add(new MeterReadingDTO(new MeterType("Hot water"), new ReadingDate(2021, 1), new ReadingData(100)));
        returnedMeterReadingDTO.add(new MeterReadingDTO(new MeterType("Gas"), new ReadingDate(2021, 1), new ReadingData(700)));

        assertThatCode(() -> assertThat(showMonthReadingsService.getUsersSpecificMonthReadings(1, 2021, "user", user))
                .containsExactlyElementsOf(returnedMeterReadingDTO)).doesNotThrowAnyException();
    }

    @Test
    void givenAnotherUserUsername_getUsersSpecificMonthReadings_shouldThrowAccessDeniedException() {
        var user = assertDoesNotThrow(() -> new User("user", "hashedPassword"));

        assertThatThrownBy(() -> showMonthReadingsService.getUsersSpecificMonthReadings(1, 2021, "anotherUser", user))
                .isInstanceOf(AccessDeniedException.class);
    }
}
