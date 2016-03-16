package fr.jayblanc.pigrows.service;

@SuppressWarnings("serial")
public class DeviceNotFoundException extends Exception {

    public DeviceNotFoundException() {
        super();
    }

    public DeviceNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DeviceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeviceNotFoundException(String message) {
        super(message);
    }

    public DeviceNotFoundException(Throwable cause) {
        super(cause);
    }
    
}
