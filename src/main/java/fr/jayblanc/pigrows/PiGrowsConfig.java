package fr.jayblanc.pigrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PiGrowsConfig {
    
    private static final Logger LOGGER = Logger.getLogger(PiGrowsConfig.class.getName());
    private static PiGrowsConfig config;
    private WatchService watcher;
    private Properties props;
    private Path home;

    private PiGrowsConfig() throws Exception {
        if ( System.getenv("PIGROWS_HOME") != null ) {
            home = Paths.get(System.getenv("PIGROWS_HOME"));
        } else if ( System.getProperty("PIGROWS_HOME") != null ) {
            home = Paths.get(System.getProperty("PIGROWS_HOME"));
        } else {
            home = Paths.get(System.getProperty("user.home"), "pigrows");
        }
        if ( !Files.exists(home) ) {
            Files.createDirectories(home);
        }
        LOGGER.log(Level.INFO, "PIGROWS_HOME set to : " + home);

        props = new Properties();
        Path configFilePath = Paths.get(home.toString(), "config.properties");
        if ( !Files.exists(configFilePath) ) {
            Files.copy(PiGrowsConfig.class.getClassLoader().getResourceAsStream("config.properties"), configFilePath);
        }
        try (InputStream in = Files.newInputStream(configFilePath) ) {
            props.load(in);
        }
        
        watcher = home.getFileSystem().newWatchService();
        WatchKey key = home.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
        PiGrowsConfigReader configWatcher = new PiGrowsConfigReader(watcher, key);
        Thread watcherThread = new Thread(configWatcher, "ConfigFileWatcher");
        watcherThread.start();
    }

    public static synchronized PiGrowsConfig getInstance() {
        if (config == null) {
            try {
                config = new PiGrowsConfig();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "unable to load configuration", e);
                throw new RuntimeException("unable to load configuration", e);
            }
        }
        return config;
    }
    
    public void shutdown() {
        try {
            watcher.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to close File Watcher");
        }
    }

    public Path getHomePath() {
        return home;
    }

    public String getProperty(PiGrowsConfig.Property property) {
        return props.getProperty(property.key());
    }

    public enum Property {

        SMTP_HOST ("smtp.host"),
        SMTP_PORT ("smtp.port"),
        SMTP_USER ("smtp.username"),
        SMTP_PASSWORD ("smtp.password"),
        
        DATE_FORMAT_PATTERN ("date.format.pattern");

        private final String key;

        private Property(String name) {
            this.key = name;
        }

        public String key() {
            return key;
        }

    }
    
    private static class PiGrowsConfigReader implements Runnable {
        
        private WatchService service;
        private WatchKey watchedKey;
        
        public PiGrowsConfigReader(WatchService watcher, WatchKey key) {
            this.service = watcher;
            this.watchedKey = key;
        }
        
        @Override
        public void run() {
            try {
                LOGGER.log(Level.INFO, "Starting Config Watcher Thread");
                WatchKey key = null;
                while(true) {
                    key = service.take();
                    if ( key.equals(watchedKey) ) {
                        Kind<?> kind = null;
                        for(WatchEvent<?> watchEvent : key.pollEvents()) {
                            kind = watchEvent.kind();
                            if (StandardWatchEventKinds.OVERFLOW == kind) {
                                continue;
                            } else if (StandardWatchEventKinds.ENTRY_MODIFY == kind) {
                                @SuppressWarnings("unchecked")
                                Path path = ((WatchEvent<Path>) watchEvent).context();
                                LOGGER.log(Level.INFO, "Path modified: " + path);
                            }
                        }
                        if(!key.reset()) {
                            break; 
                        }
                    }
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Config Watcher Thread Interrupted", e);
            } catch (ClosedWatchServiceException e) {
                LOGGER.log(Level.FINE, "WatcherService Closed");
            }
            LOGGER.log(Level.INFO, "Config Watcher Thread Stopped");
        }
    }


}
