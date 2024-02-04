package model.user;

import model.exceptions.WrongPasswordException;
import model.exceptions.WrongUsernameException;

import java.util.Objects;

import static java.util.UUID.randomUUID;

/**
 * Класс для хранения данных о пользователе
 */
public class User {
    /**
     * Идентификатор пользователя
     */
    private int userId;
    /**
     * Имя пользователя
     */
    private final String username;
    /**
     * Зашифрованный пароль
     */
    private final String encodedPassword;
    /**
     * Роль пользователя
     */
    private final UserRole role;

    /**
     * Конструктор
     * @param username имя пользователя
     * @param encodedPassword зашифрованный пароль
     */
    public User(String username, String encodedPassword) throws WrongUsernameException, WrongPasswordException {
        this.userId = randomUUID().hashCode();
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
     * Конструктор
     * @param id идентификатор пользователя
     * @param username имя пользователя
     * @param encodedPassword зашифрованный пароль
     * @param role роль пользователя
     */
    public User(int id, String username, String encodedPassword, UserRole role) throws WrongUsernameException, WrongPasswordException {
        this.userId = id;
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
     * Получить идентификатор пользователя
     * @return
     */
    public int getUserId() {
        return userId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && Objects.equals(username, user.username) && Objects.equals(encodedPassword, user.encodedPassword) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, encodedPassword, role);
    }
}
