package application.port.repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Репозиторий для сохранения аудита
 */
public interface AuditRepository {
    /**
     * Сохранить аудит
     * @param username имя пользователя
     * @param message сообщение
     */
    void saveAudit(String username, String message) throws SQLException;

    /**
     * Получить аудит
     * @return список сообщений аудита
     */
    List<String> getAudit() throws SQLException;
}
