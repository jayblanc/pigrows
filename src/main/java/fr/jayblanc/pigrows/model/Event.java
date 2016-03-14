package fr.jayblanc.pigrows.model;

import java.util.Date;

public class Event {

    private EventType type;
    private String key;
    private String message;
    private long timestamp;

    public Event() {
        super();
    }

    public Event(EventType type, String key, String message, long timestamp) {
        super();
        this.type = type;
        this.key = key;
        this.message = message;
        this.timestamp = timestamp;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "DeviceEvent [type=" + type + ", key=" + key + ", message=" + message + ", date=" + new Date(timestamp) + "]";
    }

    public enum EventType {
        ERROR, TIMEOUT, SUCCESS
    }
}
