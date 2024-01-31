package application.port.in.user;

import application.port.in.user.exceptions.UserAlreadyExistsException;
import application.port.in.user.exceptions.WrongLoginPasswordException;
import application.service.user.UnsafePasswordException;
import model.exceptions.WrongPasswordException;
import model.exceptions.WrongUsernameException;
import model.user.User;

/**
 * Сценарий: аутентификация пользователя.
 */
public interface AuthenticateUserUseCase {
    /**
     * Регистрация пользователя.
     * @param username имя пользователя
     * @param password пароль
     * @throws UserAlreadyExistsException если пользователь с таким именем уже существует
     */
    void registerUser(String username, String password) throws UserAlreadyExistsException, WrongUsernameException, WrongPasswordException, UnsafePasswordException;
    /**
     * Аутентификация пользователя.
     * @param username имя пользователя
     * @param password пароль
     * @return пользователь
     * @throws WrongLoginPasswordException если имя пользователя или пароль неверны
     */
    User loginUser(String username, String password) throws WrongLoginPasswordException;
}
