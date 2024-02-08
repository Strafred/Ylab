package application.port.in;

import application.port.in.exceptions.UserAlreadyExistsException;
import application.port.in.exceptions.WrongLoginPasswordException;
import application.service.exceptions.UnsafePasswordException;
import model.exceptions.WrongPasswordException;
import model.exceptions.WrongUsernameException;
import model.user.User;

import java.sql.SQLException;

public interface UserService {
    /**
     * Регистрация пользователя.
     * @param username имя пользователя
     * @param password пароль
     * @throws UserAlreadyExistsException если пользователь с таким именем уже существует
     */
    void registerUser(String username, String password) throws UserAlreadyExistsException, WrongUsernameException, WrongPasswordException, UnsafePasswordException, SQLException;
    /**
     * Аутентификация пользователя.
     * @param username имя пользователя
     * @param password пароль
     * @return пользователь
     * @throws WrongLoginPasswordException если имя пользователя или пароль неверны
     */
    User loginUser(String username, String password) throws WrongLoginPasswordException, SQLException, WrongUsernameException, WrongPasswordException;
}
