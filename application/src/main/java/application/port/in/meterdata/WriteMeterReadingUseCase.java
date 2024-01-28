package application.port.in.meterdata;

import application.port.in.meterdata.exceptions.AccessDeniedException;
import application.service.meterdata.exceptions.NoSuchMeterTypeException;
import model.meterdata.exceptions.DuplicateReadingException;
import model.meterdata.MeterType;
import model.meterdata.exceptions.WrongReadingValueException;
import model.user.User;

/**
 * Сценарий: запись показание счетчика
 */
public interface WriteMeterReadingUseCase {
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
}
