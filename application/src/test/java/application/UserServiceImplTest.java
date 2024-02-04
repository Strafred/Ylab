package application;

import application.port.in.exceptions.UserAlreadyExistsException;
import application.port.in.exceptions.WrongLoginPasswordException;
import application.port.repository.AuditRepository;
import application.port.repository.UserMetersRepository;
import application.port.repository.UserRepository;
import application.service.UserServiceImpl;
import model.user.User;
import model.usermeter.UserMeters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;

import static application.service.UserServiceImpl.hashPassword;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

public class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Mock
    Connection connectionMock;

    @Mock
    UserRepository userRepositoryMock;

    @Mock
    UserMetersRepository userMetersRepositoryMock;

    @Mock
    AuditRepository auditRepositoryMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenExistingUser_registerUser_shouldThrowException() {
        assertDoesNotThrow(() -> {
            Mockito.when(userRepositoryMock.userExists("test")).thenReturn(true);
        });
        assertThatExceptionOfType(UserAlreadyExistsException.class)
                .isThrownBy(() -> userServiceImpl.registerUser("test", "test"));
    }

    @Test
    void givenNonExistingUser_registerUser_shouldWorkCorrectly() {
        assertDoesNotThrow(() -> {
            userServiceImpl.registerUser("newUser", "password");

            Mockito.verify(auditRepositoryMock, times(2)).saveAudit(anyString(), anyString());
            Mockito.verify(userRepositoryMock, times(1)).userExists("newUser");
            Mockito.verify(userRepositoryMock, times(1)).postUser(any(User.class));
            Mockito.verify(userMetersRepositoryMock, times(1)).saveUserMeters(any(UserMeters.class));
        });
    }

    @Test
    void givenWrongUsername_loginUser_shouldThrowException() {
        assertDoesNotThrow(() -> {
            var user = new User("test", hashPassword("test"));
            Mockito.when(userRepositoryMock.getUser("test")).thenReturn(user);
        });

        assertThatExceptionOfType(WrongLoginPasswordException.class)
                .isThrownBy(() -> userServiceImpl.loginUser("wrongUsername", "test"));
    }

    @Test
    void givenWrongPassword_loginUser_shouldThrowException() {
        assertDoesNotThrow(() -> {
            var user = new User("test", hashPassword("test"));
            Mockito.when(userRepositoryMock.getUser("test")).thenReturn(user);
        });

        assertThatExceptionOfType(WrongLoginPasswordException.class)
                .isThrownBy(() -> userServiceImpl.loginUser("test", "wrongPassword"));
    }

    @Test
    void givenCorrectCredentials_loginUser_shouldWorkCorrectly() {
        assertDoesNotThrow(() -> {
            var user = new User("test", hashPassword("test"));
            Mockito.when(userRepositoryMock.getUser("test")).thenReturn(user);

            userServiceImpl.loginUser("test", "test");

            Mockito.verify(auditRepositoryMock, times(2)).saveAudit(anyString(), anyString());
            Mockito.verify(userRepositoryMock, times(1)).getUser("test");
        });
    }
}
