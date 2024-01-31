package application.port.in.meterdata;

import application.port.in.meterdata.dto.MeterReadingDTO;
import application.port.in.meterdata.exceptions.AccessDeniedException;
import model.user.User;

import java.util.List;

/**
 * Сценарий: получение показаний счетчика за определенный месяц
 */
public interface ShowMonthReadingsUseCase {
    /**
     * Получить показания счетчика за определенный месяц
     * @param month месяц
     * @param year год
     * @param username имя пользователя
     * @param loggedInUser авторизованный пользователь
     * @return список показаний счетчика
     * @throws AccessDeniedException если авторизованный пользователь не имеет права на просмотр показаний счетчика пользователя
     */
    List<MeterReadingDTO> getUsersSpecificMonthReadings(int month, int year, String username, User loggedInUser) throws AccessDeniedException;

    /**
     * Получить показания счетчика за текущий месяц
     * @param username имя пользователя
     * @param loggedInUser авторизованный пользователь
     * @return список показаний счетчика
     * @throws AccessDeniedException если авторизованный пользователь не имеет права на просмотр показаний счетчика пользователя
     */
    List<MeterReadingDTO> getUsersCurrentMonthReadings(String username, User loggedInUser) throws AccessDeniedException;
}
