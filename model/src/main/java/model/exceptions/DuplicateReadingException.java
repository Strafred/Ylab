package model.exceptions;

/**
 * Исключение, возникающее при попытке добавить показание счетчика, когда за этот месяц уже есть показание
 */
public class DuplicateReadingException extends Exception {
    public DuplicateReadingException(String message) {
        super(message);
    }
}
