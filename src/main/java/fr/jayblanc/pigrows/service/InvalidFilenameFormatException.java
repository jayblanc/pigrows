package fr.jayblanc.pigrows.service;

public class InvalidFilenameFormatException extends Exception {

    public InvalidFilenameFormatException() {
    }

    public InvalidFilenameFormatException(String message) {
        super(message);
    }

    public InvalidFilenameFormatException(Throwable cause) {
        super(cause);
    }

    public InvalidFilenameFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFilenameFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
