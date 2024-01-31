package application;

import application.port.in.exceptions.UserAlreadyExistsException;
import application.port.in.exceptions.WrongLoginPasswordException;
import application.port.out.AuditRepository;
import application.port.out.UserMetersRepository;
import application.port.out.UserRepository;
import application.service.UserServiceImpl;
import model.user.User;
import model.usermeter.UserMeters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
                .isThrownBy(() -> userServiceImpl.registerUser("test", "test"));
    }

    @Test
    void givenNonExistingUser_registerUser_shouldWorkCorrectly() {
        assertDoesNotThrow(() -> userServiceImpl.registerUser("newUser", "password"));

        Mockito.verify(auditRepositoryMock, times(2)).saveAudit(anyString(), anyString());
        Mockito.verify(userRepositoryMock, times(1)).userExists("newUser");
        Mockito.verify(userRepositoryMock, times(1)).saveUser(any(User.class));
        Mockito.verify(userMetersRepositoryMock, times(1)).saveUserMeters(any(UserMeters.class));
    }

    @Test
    void givenWrongUsername_loginUser_shouldThrowException() {
        var user = assertDoesNotThrow(() -> new User("test", hashPassword("test")));
        Mockito.when(userRepositoryMock.getUser("test")).thenReturn(user);

        assertThatExceptionOfType(WrongLoginPasswordException.class)
                .isThrownBy(() -> userServiceImpl.loginUser("wrongUsername", "test"));
    }

    @Test
    void givenWrongPassword_loginUser_shouldThrowException() {
        var user = assertDoesNotThrow(() -> new User("test", hashPassword("test")));
        Mockito.when(userRepositoryMock.getUser("test")).thenReturn(user);

        assertThatExceptionOfType(WrongLoginPasswordException.class)
                .isThrownBy(() -> userServiceImpl.loginUser("test", "wrongPassword"));
    }

    @Test
    void givenCorrectCredentials_loginUser_shouldWorkCorrectly() {
        var user = assertDoesNotThrow(() -> new User("test", hashPassword("test")));
        Mockito.when(userRepositoryMock.getUser("test")).thenReturn(user);

        assertDoesNotThrow(() -> userServiceImpl.loginUser("test", "test"));

        Mockito.verify(auditRepositoryMock, times(2)).saveAudit(anyString(), anyString());
        Mockito.verify(userRepositoryMock, times(1)).getUser("test");
    }
}
