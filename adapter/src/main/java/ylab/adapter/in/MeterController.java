package ylab.adapter.in;

import application.port.in.MeterService;
import application.port.in.dto.MeterDataDTO;
import application.port.in.dto.MeterReadingDTO;
import application.port.in.exceptions.AccessDeniedException;
import application.service.exceptions.NoSuchMeterTypeException;
import model.exceptions.WrongPasswordException;
import model.exceptions.WrongUsernameException;
import model.meterdata.MeterType;
import model.exceptions.DuplicateReadingException;
import model.exceptions.WrongReadingValueException;
import model.user.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Контроллер для работы с данными о счетчиках
 */
public class MeterController {
    MeterService meterService;

    public MeterController(MeterService meterService) {
        this.meterService = meterService;
    }

    /**
     * Эндпоинт для получения списка доступных типов счетчиков
     * @return список доступных типов счетчиков
     */
    public List<MeterType> getAccessibleMeterTypes() {
        try {
            return meterService.getAccessibleMeterTypes();
        } catch (SQLException e) {
            System.err.println("SQL exception");
        }
        return null;
    }

    /**
     * Эндпоинт для получения истории показаний счетчиков
     * @param username имя пользователя
     * @param loggedInUser авторизованный пользователь
     * @return список показаний счетчиков пользователя
     */
    public List<MeterDataDTO> showMetersHistory(String username, User loggedInUser) {
        try {
            return meterService.getMetersHistory(username, loggedInUser);
        } catch (AccessDeniedException | SQLException | WrongUsernameException | WrongPasswordException e) {
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
            return meterService.getUsersCurrentMonthReadings(username, loggedInUser);
        } catch (AccessDeniedException | WrongUsernameException | SQLException | WrongPasswordException e) {
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
            return meterService.getUsersSpecificMonthReadings(month, year, username, loggedInUser);
        } catch (AccessDeniedException | SQLException | WrongUsernameException | WrongPasswordException e) {
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
            meterService.writeMeterReading(meterType, readingValue, username, loggedInUser);
        } catch (DuplicateReadingException e) {
            System.err.println("Duplicate reading");
        } catch (WrongReadingValueException e) {
            System.err.println("Wrong reading value");
        } catch (AccessDeniedException e) {
            System.err.println("Access denied");
        } catch (NoSuchMeterTypeException e) {
            System.err.println("No such method type");
        } catch (WrongUsernameException e) {
            System.err.println("Wrong username");
        } catch (SQLException e) {
            System.err.println("SQL exception");
        } catch (WrongPasswordException e) {
            System.err.println("Wrong password");
        }
    }

    /**
     * Эндпоинт для добавления нового типа счетчика
     * @param meterTypeName имя нового типа счетчика
     * @param loggedInUser авторизованный пользователь
     */
    public void addNewMeterType(String meterTypeName, User loggedInUser) {
        try {
            meterService.addNewMeterType(meterTypeName, loggedInUser);
        } catch (AccessDeniedException | SQLException e) {
            System.err.println("Access denied");
        }
    }
}
