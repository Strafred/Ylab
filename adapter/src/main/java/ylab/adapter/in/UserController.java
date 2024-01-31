package ylab.adapter.in;

import application.port.in.user.AuthenticateUserUseCase;
import application.port.in.user.exceptions.UserAlreadyExistsException;
import application.port.in.user.exceptions.WrongLoginPasswordException;
import model.user.User;

/**
 * Контроллер для получения данных о пользователях
 */
public class UserController {
    AuthenticateUserUseCase authenticateUserUseCase;

    public UserController(AuthenticateUserUseCase authenticateUserUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    /**
     * Эндпоинт для регистрации пользователя
     * @param username имя пользователя
     * @param password пароль пользователя
     */
    public void registerUser(String username, String password) {
        try {
            authenticateUserUseCase.registerUser(username, password);
        } catch (UserAlreadyExistsException e) {
            System.err.println("User already exists!");
        }
    }

    /**
     * Эндпоинт для авторизации пользователя
     * @param username имя пользователя
     * @param password пароль пользователя
     * @return объект пользователя
     */
    public User loginUser(String username, String password) {
        try {
            return authenticateUserUseCase.loginUser(username, password);
        } catch (WrongLoginPasswordException e) {
            System.err.println("Wrong login or password!");
        }
        return null;
    }
}
