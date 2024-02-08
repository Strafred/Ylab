package application.port.repository;

import model.exceptions.WrongPasswordException;
import model.exceptions.WrongUsernameException;
import model.user.User;

import java.sql.SQLException;

/**
 * Интерфейс для работы с репозиторием пользователей
 */
public interface UserRepository {
    /**
     * Сохранить пользователя
     *
     * @param user пользователь
     * @return
     */
    User postUser(User user) throws SQLException, WrongUsernameException, WrongPasswordException;
    /**
     * Получить пользователя по имени
     * @param username имя пользователя
     * @return пользователь
     */
    User getUser(String username) throws SQLException, WrongUsernameException, WrongPasswordException;
    /**
     * Проверить существование пользователя
     * @param username имя пользователя
     * @return true, если пользователь существует
     */
    boolean userExists(String username) throws SQLException;
}
