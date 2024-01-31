package model.meterdata.exceptions;

/**
 * Исключение, возникающее при некорректном значении поданного показания счетчика
 */
public class WrongReadingValueException extends Exception {
    public WrongReadingValueException(String message) {
        super(message);
    }
}
