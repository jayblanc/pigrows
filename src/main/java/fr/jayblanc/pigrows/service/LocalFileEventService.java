package fr.jayblanc.pigrows.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.jayblanc.pigrows.PiGrowsConfig;
import fr.jayblanc.pigrows.model.Event;
import fr.jayblanc.pigrows.model.Event.EventType;

public class LocalFileEventService implements EventService {

    private Path store;

    private LocalFileEventService() {
        store = Paths.get(PiGrowsConfig.getInstance().getHomePath().toString(), "events.log");
        if ( !Files.exists(store) ) {
            try {
                Files.createFile(store);
            } catch (IOException e) {
                //
            }
        }
    }

    private static class LocalFileEventServiceHolder {
        private final static LocalFileEventService instance = new LocalFileEventService();
    }

    public static LocalFileEventService getInstance() {
        return LocalFileEventServiceHolder.instance;
    }

    @Override
    public void append(String key, String type, String message) throws IOException {
        Event event = new Event(key, EventType.valueOf(type), message);
        Files.write(store, event.serialize().getBytes(), StandardOpenOption.APPEND);
    }
    
    @Override
    public long count(String key) throws IOException {
        try (Stream<String> lines = Files.lines(store)) {
            Stream<Event> stream = lines.map(Event::deserialize);
            if ( key != null && key.length() > 0 ) {
                stream = stream.filter(e -> e.getKey().equals(key));
            }
            long count = stream.count();
            stream.close();
            return count;
        }
    }
    
    @Override
    public List<Event> find(String key, int offset, int limit) throws IOException {
        try (Stream<String> lines = Files.lines(store)) {
            Stream<Event> stream = lines.map(Event::deserialize);
            if ( key != null && key.length() > 0 ) {
                stream = stream.filter(e -> e.getKey().equals(key));
            }
            if ( offset > 0 ) {
                stream = stream.skip(offset);
            }
            if ( limit > 0 ) {
                stream = stream.limit(limit);
            }
            List<Event> sendback = stream.collect(Collectors.toList());
            stream.close();
            return sendback;
        }
    }

    @Override
    public String read(int offset, int limit) throws IOException {
        Stream<String> stream = Files.lines(store);
        if ( offset > 0 ) {
            stream = stream.skip(offset);
        }
        if ( limit > 0 ) {
            stream = stream.limit(limit);
        }
        String sendback =  stream.map(s -> s.concat("\r\n")).collect(Collectors.joining());
        stream.close();
        return sendback;
    }
    
    @Override
    public Path readAll() {
        return store;
    }

    @Override
    public void purge() throws IOException {
        Files.write(store, new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
    }
}
