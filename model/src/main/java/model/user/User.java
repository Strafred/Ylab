package model.user;

import model.exceptions.WrongPasswordException;
import model.exceptions.WrongUsernameException;

import static java.util.UUID.randomUUID;

/**
 * Класс для хранения данных о пользователе
 */
public class User {
    /**
     * Идентификатор пользователя
     */
    String userId;
    /**
     * Имя пользователя
     */
    String username;
    /**
     * Зашифрованный пароль
     */
    String encodedPassword;
    /**
     * Роль пользователя
     */
    UserRole role;

    /**
     * Конструктор
     * @param username имя пользователя
     * @param encodedPassword зашифрованный пароль
     */
    public User(String username, String encodedPassword) throws WrongUsernameException, WrongPasswordException {
        this.userId = randomUUID().toString();
        if (StringUtils.isEmpty(username)) {
            throw new WrongUsernameException("Username cannot be empty!");
        }
        this.username = username;
        if (StringUtils.isEmpty(encodedPassword)) {
            throw new WrongPasswordException("Password cannot be empty!");
        }
        this.encodedPassword = encodedPassword;
        this.role = UserRole.USER;
    }

    /**
     * Конструктор
     * @param username имя пользователя
     * @param encodedPassword зашифрованный пароль
     * @param role роль пользователя
     */
    public User(String username, String encodedPassword, UserRole role) throws WrongUsernameException, WrongPasswordException {
        if (StringUtils.isEmpty(username)) {
            throw new WrongUsernameException("Username cannot be empty!");
        }
        this.username = username;
        if (StringUtils.isEmpty(encodedPassword)) {
            throw new WrongPasswordException("Password cannot be empty!");
        }
        this.encodedPassword = encodedPassword;
        this.role = role;
    }

    /**
     * Получить имя пользователя
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Получить зашифрованный пароль
     * @return
     */
    public String getEncodedPassword() {
        return encodedPassword;
    }

    /**
     * Получить роль пользователя
     * @return
     */
    public UserRole getRole() {
        return role;
    }
}
