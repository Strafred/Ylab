package application;

import application.port.in.meterdata.dto.MeterDataDTO;
import application.port.in.meterdata.exceptions.AccessDeniedException;
import application.port.out.AuditRepository;
import application.port.out.UserMetersRepository;
import application.service.meterdata.ShowMetersHistoryService;
import model.meterdata.MeterData;
import model.meterdata.MeterType;
import model.user.User;
import model.usermeter.UserMeters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static application.service.user.AuthenticateUserService.hashPassword;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ShowMetersHistoryServiceTest {
    @InjectMocks
    ShowMetersHistoryService showMetersHistoryService;

    @Mock
    UserMetersRepository userMetersRepository;

    @Mock
    AuditRepository auditRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenAnotherUserUsername_getMetersHistory_shouldThrowAccessDeniedException() {
        User loggedInUser = assertDoesNotThrow(() -> new User("test", hashPassword("test")));
        String username = "anotherUser";

        assertThatThrownBy(() -> showMetersHistoryService.getMetersHistory(username, loggedInUser))
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

        Mockito.when(userMetersRepository.getUserMetersByUsername(username))
                .thenReturn(userMeters);

        MeterDataDTO returnedMeterDataDTO = new MeterDataDTO(meterType, new HashMap<>());

        assertThatCode(() -> {
            assertThat(showMetersHistoryService.getMetersHistory(username, loggedInUser).size()).isEqualTo(1);
            assertThat(showMetersHistoryService.getMetersHistory(username, loggedInUser).get(0)).isEqualTo(returnedMeterDataDTO);
        }).doesNotThrowAnyException();
    }
}
