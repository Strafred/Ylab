package application.port.repository;

import model.exceptions.WrongPasswordException;
import model.exceptions.WrongUsernameException;
import model.meterdata.MeterData;
import model.usermeter.UserMeters;

import java.sql.SQLException;

/**
 * Интерфейс для работы с репозиторием пользовательских счетчиков
 */
public interface UserMetersRepository {
    /**
     * Сохранить пользовательские счетчики
     * @param userMeters пользовательские счетчики
     */
    void saveUserMeters(UserMeters userMeters) throws SQLException;
    /**
     * Получить пользовательские счетчики по имени пользователя
     * @param username имя пользователя
     * @return пользовательские счетчики
     */
    UserMeters getUserMetersByUsername(String username) throws SQLException, WrongUsernameException, WrongPasswordException;
    /**
     * Проверить существование пользовательских счетчиков
     * @param username имя пользователя
     * @return true, если пользовательские счетчики существуют
     */
    void putUserMeterByUsername(String username, MeterData meterData) throws SQLException;
}
