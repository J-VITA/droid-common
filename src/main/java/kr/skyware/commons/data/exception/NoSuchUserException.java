package kr.skyware.commons.data.exception;

public class NoSuchUserException extends Exception{

    public NoSuchUserException() {
        super();
    }

    public NoSuchUserException(String message) {
        super(message);
    }

    public NoSuchUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
