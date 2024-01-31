package application.port.out;

import model.meterdata.MeterData;
import model.usermeter.UserMeters;

/**
 * Интерфейс для работы с репозиторием пользовательских счетчиков
 */
public interface UserMetersRepository {
    /**
     * Сохранить пользовательские счетчики
     * @param userMeters пользовательские счетчики
     */
    void saveUserMeters(UserMeters userMeters);
    /**
     * Получить пользовательские счетчики по имени пользователя
     * @param username имя пользователя
     * @return пользовательские счетчики
     */
    UserMeters getUserMetersByUsername(String username);
    /**
     * Проверить существование пользовательских счетчиков
     * @param username имя пользователя
     * @return true, если пользовательские счетчики существуют
     */
    void putUserMeterByUsername(String username, MeterData meterData);
}
