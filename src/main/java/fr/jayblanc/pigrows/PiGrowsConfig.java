package fr.jayblanc.pigrows;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PiGrowsConfig {
    
    private static final Logger LOGGER = Logger.getLogger(PiGrowsConfig.class.getName());
    private static PiGrowsConfig config;
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
        init();
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
    }

    public Path getHomePath() {
        return home;
    }

    public String getProperty(PiGrowsConfig.Property property) {
        return props.getProperty(property.key());
    }
    
    public void setProperty(PiGrowsConfig.Property property, String value) throws IOException {
        props.setProperty(property.key(), value);
        save();
    }
    
    public void setProperties(Map<PiGrowsConfig.Property, String> properties) throws IOException {
        for ( Entry<PiGrowsConfig.Property, String> entry : properties.entrySet() ) {
            props.setProperty(entry.getKey().key(), entry.getValue());
        }
        save();
    }
    
    public Properties listProperties() {
        return props;
    }
    
    private void init() throws IOException {
        Path configFilePath = Paths.get(home.toString(), "config.properties");
        if ( !Files.exists(configFilePath) ) {
            Files.copy(PiGrowsConfig.class.getClassLoader().getResourceAsStream("config.properties"), configFilePath);
        }
        try (InputStream in = Files.newInputStream(configFilePath) ) {
            props.load(in);
        }
    }
    
    private void load() throws IOException {
        Path configFilePath = Paths.get(home.toString(), "config.properties");
        try (InputStream in = Files.newInputStream(configFilePath) ) {
            props.load(in);
        }
    }
    
    private void save() throws IOException {
        Path configFilePath = Paths.get(home.toString(), "config.properties");
        try (OutputStream out = Files.newOutputStream(configFilePath) ) {
            props.store(out, "AUTO GENERATED FILE, DO NOT MODIFY MANUALLY");
        }
    }

    public enum Property {

        SMTP_HOST ("smtp.host"),
        SMTP_PORT ("smtp.port"),
        SMTP_USER ("smtp.username"),
        SMTP_PASSWORD ("smtp.password"),
        
        NOTIFICATION_RECIPIENTS ("notification.recipients");
        
        private final String key;

        private Property(String name) {
            this.key = name;
        }

        public String key() {
            return key;
        }

    }
    
}
