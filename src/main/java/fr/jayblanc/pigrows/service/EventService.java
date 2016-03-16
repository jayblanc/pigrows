package fr.jayblanc.pigrows.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import fr.jayblanc.pigrows.model.Event;

public interface EventService {

    void append(String key, String type, String message) throws UnsupportedEncodingException, IOException;
    
    long count(String key) throws IOException;

    List<Event> find(String key, int offset, int limit) throws IOException;

    String read(int offset, int limit) throws IOException;

}
