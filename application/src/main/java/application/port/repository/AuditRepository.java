package application.port.repository;

/**
 * Репозиторий для сохранения аудита
 */
public interface AuditRepository {
    /**
     * Сохранить аудит
     * @param username имя пользователя
     * @param message сообщение
     */
    void saveAudit(String username, String message);
}
