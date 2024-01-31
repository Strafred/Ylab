package application.service.user;

public class UnsafePasswordException extends Throwable {
    public UnsafePasswordException(String message) {
        super(message);
    }
}
