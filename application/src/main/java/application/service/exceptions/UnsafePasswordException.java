package application.service.exceptions;

public class UnsafePasswordException extends Throwable {
    public UnsafePasswordException(String message) {
        super(message);
    }
}
