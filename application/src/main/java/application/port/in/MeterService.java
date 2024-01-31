package application.port.in;

import application.port.in.dto.MeterDataDTO;
import application.port.in.dto.MeterReadingDTO;
import application.port.in.exceptions.AccessDeniedException;
import application.service.exceptions.NoSuchMeterTypeException;
import model.exceptions.DuplicateReadingException;
import model.exceptions.WrongReadingValueException;
import model.meterdata.MeterType;
import model.user.User;

import java.util.List;

public interface MeterService {
    /**
     * Получить список доступных типов счетчиков
     * @return список доступных типов счетчиков
     */
    List<MeterType> getAccessibleMeterTypes();

    /**
     * Просмотр истории показаний счетчиков
     * @param username - имя пользователя
     * @param loggedInUser - текущий пользователь
     * @return - список показаний счетчиков
     * @throws AccessDeniedException - если текущий пользователь не имеет права просматривать историю показаний счетчиков пользователя
     */
    List<MeterDataDTO> getMetersHistory(String username, User loggedInUser) throws AccessDeniedException;

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

    /**
     * Записать показание счетчика
     * @param meterType    тип счетчика
     * @param readingValue показание счетчика
     * @param username     логин пользователя, которому принадлежит счетчик
     * @param loggedInUser авторизованный пользователь
     * @throws DuplicateReadingException если показание уже было записано
     * @throws WrongReadingValueException если показание некорректно
     * @throws AccessDeniedException если авторизованный пользователь не имеет права записывать показания счетчика
     * @throws NoSuchMeterTypeException если тип счетчика не найден
     */
    void writeMeterReading(MeterType meterType, int readingValue, String username, User loggedInUser) throws DuplicateReadingException, WrongReadingValueException, AccessDeniedException, NoSuchMeterTypeException, NoSuchMeterTypeException;

    /**
     * Добавить новый тип счетчика
     * @param meterTypeName название типа счетчика
     * @param loggedInUser авторизованный пользователь
     * @throws AccessDeniedException если авторизованный пользователь не имеет права добавлять новый тип счетчика
     */
    void addNewMeterType(String meterTypeName, User loggedInUser) throws AccessDeniedException;
}
