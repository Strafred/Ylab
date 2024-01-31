package application.port.repository;

import model.user.User;

/**
 * Интерфейс для работы с репозиторием пользователей
 */
public interface UserRepository {
    /**
     * Сохранить пользователя
     * @param user пользователь
     */
    void saveUser(User user);
    /**
     * Получить пользователя по имени
     * @param username имя пользователя
     * @return пользователь
     */
    User getUser(String username);
    /**
     * Проверить существование пользователя
     * @param username имя пользователя
     * @return true, если пользователь существует
     */
    boolean userExists(String username);
}
