package fr.jayblanc.pigrows.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class Event {

    private static final DateFormat sdf = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT);
    private static final String SEPARATOR = ",";
    
    private String key;
    private EventType type;
    private String message;
    private String date;

    public Event() {
        super();
        this.date = sdf.format(new Date(System.currentTimeMillis()));
    }

    public Event(String key, EventType type, String message) {
        this();
        this.type = type;
        this.key = key;
        this.message = message;
    }
    
    private Event(String key, EventType type, String message, String date) {
        this(key, type, message);
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public String serialize() {
        StringBuilder builder = new StringBuilder();
        builder.append(date).append(SEPARATOR).append(key).append(SEPARATOR).append(type).append(SEPARATOR).append(message).append("\r\n");
        return builder.toString();
    }
    
    public static Event deserialize(String serializedEvent) {
        StringTokenizer strtok = new StringTokenizer(serializedEvent, SEPARATOR);
        String date = strtok.nextToken();
        String key = strtok.nextToken();
        EventType type = EventType.valueOf(strtok.nextToken());
        String message = strtok.nextToken();
        return new Event(key, type, message, date);
    }

    public enum EventType {
        WAKEUP, START, ACTION, ERROR, SUCCESS, SHUTDOWN
    }
}
