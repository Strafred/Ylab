package application;

import application.port.in.user.exceptions.UserAlreadyExistsException;
import application.port.in.user.exceptions.WrongLoginPasswordException;
import application.port.out.AuditRepository;
import application.port.out.UserMetersRepository;
import application.port.out.UserRepository;
import application.service.user.AuthenticateUserService;
import model.user.User;
import model.usermeter.UserMeters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static application.service.user.AuthenticateUserService.hashPassword;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

public class AuthenticateUserServiceTest {
    @InjectMocks
    AuthenticateUserService authenticateUserService;

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
        Mockito.when(userRepositoryMock.userExists("test")).thenReturn(true);

        assertThatExceptionOfType(UserAlreadyExistsException.class)
                .isThrownBy(() -> authenticateUserService.registerUser("test", "test"));
    }

    @Test
    void givenNonExistingUser_registerUser_shouldWorkCorrectly() {
        assertDoesNotThrow(() -> authenticateUserService.registerUser("newUser", "password"));

        Mockito.verify(auditRepositoryMock, times(2)).saveAudit(anyString(), anyString());
        Mockito.verify(userRepositoryMock, times(1)).userExists("newUser");
        Mockito.verify(userRepositoryMock, times(1)).saveUser(any(User.class));
        Mockito.verify(userMetersRepositoryMock, times(1)).saveUserMeters(any(UserMeters.class));
    }

    @Test
    void givenWrongUsername_loginUser_shouldThrowException() {
        Mockito.when(userRepositoryMock.getUser("test")).thenReturn(new User("test", hashPassword("test")));

        assertThatExceptionOfType(WrongLoginPasswordException.class)
                .isThrownBy(() -> authenticateUserService.loginUser("wrongUsername", "test"));
    }

    @Test
    void givenWrongPassword_loginUser_shouldThrowException() {
        Mockito.when(userRepositoryMock.getUser("test")).thenReturn(new User("test", hashPassword("test")));

        assertThatExceptionOfType(WrongLoginPasswordException.class)
                .isThrownBy(() -> authenticateUserService.loginUser("test", "wrongPassword"));
    }

    @Test
    void givenCorrectCredentials_loginUser_shouldWorkCorrectly() {
        Mockito.when(userRepositoryMock.getUser("test")).thenReturn(new User("test", hashPassword("test")));

        assertDoesNotThrow(() -> authenticateUserService.loginUser("test", "test"));

        Mockito.verify(auditRepositoryMock, times(2)).saveAudit(anyString(), anyString());
        Mockito.verify(userRepositoryMock, times(1)).getUser("test");
    }
}
