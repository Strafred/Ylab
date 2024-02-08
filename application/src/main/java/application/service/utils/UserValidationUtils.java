package application.service.utils;

import model.user.User;
import model.user.UserRole;

/**
 * Утилиты для проверки прав доступа к данным пользователя
 */
public class UserValidationUtils {
    /**
     * Проверить, имеет ли пользователь доступ к данным пользователя
     * @param username имя пользователя
     * @param loggedInUser данные о текущем пользователе
     * @return true, если пользователь имеет доступ к данным пользователя
     */
    public static boolean haveAccessToUser(String username, User loggedInUser) {
        return loggedInUser.getUsername().equals(username) || loggedInUser.getRole().equals(UserRole.ADMIN);
    }

    public static boolean isAdmin(User loggedInUser) {
        return loggedInUser.getRole().equals(UserRole.ADMIN);
    }
}
