package ylab.adapter.in;

import application.port.in.meterdata.GetAccessibleMeterTypesUseCase;
import application.port.in.meterdata.ShowMetersHistoryUseCase;
import application.port.in.meterdata.ShowMonthReadingsUseCase;
import application.port.in.meterdata.WriteMeterReadingUseCase;
import application.port.in.meterdata.dto.MeterDataDTO;
import application.port.in.meterdata.dto.MeterReadingDTO;
import application.port.in.meterdata.exceptions.AccessDeniedException;
import application.service.meterdata.exceptions.NoSuchMeterTypeException;
import model.meterdata.MeterType;
import model.exceptions.DuplicateReadingException;
import model.exceptions.WrongReadingValueException;
import model.user.User;

import java.util.List;

/**
 * Контроллер для работы с данными о счетчиках
 */
public class MeterController {
    GetAccessibleMeterTypesUseCase getAccessibleMeterTypesUseCase;
    ShowMetersHistoryUseCase showMetersHistoryUseCase;
    ShowMonthReadingsUseCase showMonthReadingsUseCase;
    WriteMeterReadingUseCase writeMeterReadingUseCase;

    public MeterController(GetAccessibleMeterTypesUseCase getAccessibleMeterTypesUseCase,
                           ShowMetersHistoryUseCase showMetersHistoryUseCase,
                           ShowMonthReadingsUseCase showMonthReadingsUseCase,
                           WriteMeterReadingUseCase writeMeterReadingUseCase) {
        this.getAccessibleMeterTypesUseCase = getAccessibleMeterTypesUseCase;
        this.showMetersHistoryUseCase = showMetersHistoryUseCase;
        this.showMonthReadingsUseCase = showMonthReadingsUseCase;
        this.writeMeterReadingUseCase = writeMeterReadingUseCase;
    }

    /**
     * Эндпоинт для получения списка доступных типов счетчиков
     * @return список доступных типов счетчиков
     */
    public List<MeterType> getAccessibleMeterTypes() {
        return getAccessibleMeterTypesUseCase.getAccessibleMeterTypes();
    }

    /**
     * Эндпоинт для получения истории показаний счетчиков
     * @param username имя пользователя
     * @param loggedInUser авторизованный пользователь
     * @return список показаний счетчиков пользователя
     */
    public List<MeterDataDTO> showMetersHistory(String username, User loggedInUser) {
        try {
            return showMetersHistoryUseCase.getMetersHistory(username, loggedInUser);
        } catch (AccessDeniedException e) {
            System.err.println("Access denied");
        }
        return null;
    }

    /**
     * Эндпоинт для получения показаний счетчиков за текущий месяц
     * @param username имя пользователя
     * @param loggedInUser авторизованный пользователь
     * @return список показаний счетчиков пользователя за текущий месяц
     */
    public List<MeterReadingDTO> showMonthReadings(String username, User loggedInUser) {
        try {
            return showMonthReadingsUseCase.getUsersCurrentMonthReadings(username, loggedInUser);
        } catch (AccessDeniedException e) {
            System.err.println("Access denied");
        }
        return null;
    }

    /**
     * Эндпоинт для получения показаний счетчиков за указанный месяц
     * @param month месяц
     * @param year год
     * @param username имя пользователя
     * @param loggedInUser авторизованный пользователь
     * @return список показаний счетчиков пользователя за указанный месяц
     */
    public List<MeterReadingDTO> getUsersSpecificMonthReadings(int month, int year, String username, User loggedInUser) {
        try {
            return showMonthReadingsUseCase.getUsersSpecificMonthReadings(month, year, username, loggedInUser);
        } catch (AccessDeniedException e) {
            System.err.println("Access denied");
        }
        return null;
    }

    /**
     * Эндпоинт для записи показаний счетчика
     * @param meterType тип счетчика
     * @param readingValue показание счетчика
     * @param username имя пользователя
     * @param loggedInUser авторизованный пользователь
     */
    public void writeMeterReading(MeterType meterType, int readingValue, String username, User loggedInUser) {
        try {
            writeMeterReadingUseCase.writeMeterReading(meterType, readingValue, username, loggedInUser);
        } catch (DuplicateReadingException e) {
            System.err.println("Duplicate reading");
        } catch (WrongReadingValueException e) {
            System.err.println("Wrong reading value");
        } catch (AccessDeniedException e) {
            System.err.println("Access denied");
        } catch (NoSuchMeterTypeException e) {
            System.err.println("No such method type");
        }
    }
}
