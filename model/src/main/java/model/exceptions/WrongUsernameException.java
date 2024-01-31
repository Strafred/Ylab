package model.exceptions;

public class WrongUsernameException extends Exception {
    public WrongUsernameException(String message) {
        super(message);
    }
}
