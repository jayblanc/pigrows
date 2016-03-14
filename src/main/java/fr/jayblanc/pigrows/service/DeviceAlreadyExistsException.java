package fr.jayblanc.pigrows.service;

@SuppressWarnings("serial")
public class DeviceAlreadyExistsException extends Exception {

    public DeviceAlreadyExistsException() {
        super();
    }

    public DeviceAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DeviceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeviceAlreadyExistsException(String message) {
        super(message);
    }

    public DeviceAlreadyExistsException(Throwable cause) {
        super(cause);
    }
    
}
