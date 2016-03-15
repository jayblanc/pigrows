package fr.jayblanc.pigrows.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import fr.jayblanc.pigrows.PiGrowsConfig;
import fr.jayblanc.pigrows.model.Device;

public class LocalFileDeviceService implements DeviceService {

    private static final Logger LOGGER = Logger.getLogger(LocalFileDeviceService.class.getName());

    private Gson gson;
    private Map<String, Device> devices;
    private Path store;

    private LocalFileDeviceService() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        store = Paths.get(PiGrowsConfig.getInstance().getHomePath().toString(), "devices.txt");
        loadFromStore();
    }

    private static class FileDeviceServiceHolder {
        private final static LocalFileDeviceService instance = new LocalFileDeviceService();
    }

    public static LocalFileDeviceService getInstance() {
        return FileDeviceServiceHolder.instance;
    }

    private void loadFromStore() {
        try {
            if (Files.notExists(store)) {
                devices = new HashMap<String, Device>();
                saveToStore();
            } else {
                String content = new String(Files.readAllBytes(store), "UTF-8");
                if (content.length() == 0) {
                    devices = new HashMap<String, Device>();
                } else {
                    devices = gson.fromJson(new String(Files.readAllBytes(store), "UTF-8"), new TypeToken<Map<String, Device>>() {}.getType());
                }
            }
        } catch (JsonSyntaxException | IOException e) {
            devices = new HashMap<String, Device>();
            LOGGER.log(Level.SEVERE, "unable to load devices", e);
        }
    }

    private void saveToStore() {
        try {
            LOGGER.log(Level.FINE, "Syncing devices with FileStore");
            String jsonDevices = gson.toJson(devices);
            Files.write(store, jsonDevices.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            LOGGER.log(Level.FINE, "FileStore devices synced");
        } catch (JsonSyntaxException | IOException e) {
            LOGGER.log(Level.SEVERE, "unable to load devices", e);
        }
    }

    @Override
    public Collection<Device> listAll() {
        return devices.values();
    }

    @Override
    public void create(String key, String name, String description) throws DeviceAlreadyExistsException {
        LOGGER.log(Level.FINE, "Creating device: " + key);
        if (devices.containsKey(key)) {
            throw new DeviceAlreadyExistsException("A device already exists for key: " + key);
        }
        Device device = new Device(key, name, description);
        this.devices.put(key, device);
        saveToStore();
    }

    @Override
    public Device get(String key) throws DeviceNotFoundException {
        if (!devices.containsKey(key)) {
            throw new DeviceNotFoundException("Unable to find a device with key: " + key);
        }
        return devices.get(key);
    }

    @Override
    public void update(String key, String name, String description) throws DeviceNotFoundException {
        LOGGER.log(Level.FINE, "Updating device: " + key);
        if (!devices.containsKey(key)) {
            throw new DeviceNotFoundException("Unable to find a device with key: " + key);
        }
        devices.get(key).setName(name);
        devices.get(key).setDescription(description);
        saveToStore();
    }

    @Override
    public void updateActivity(String key) throws DeviceNotFoundException {
        LOGGER.log(Level.FINE, "Updating device activity timestamp: " + key);
        if (!devices.containsKey(key)) {
            throw new DeviceNotFoundException("Unable to find a device with key: " + key);
        }
        devices.get(key).setLastActivity(System.currentTimeMillis());
        saveToStore();
    }

    @Override
    public void delete(String key) throws DeviceNotFoundException {
        LOGGER.log(Level.FINE, "Deleting device: " + key);
        if (!devices.containsKey(key)) {
            throw new DeviceNotFoundException("Unable to find a device with key: " + key);
        }
        devices.remove(key);
        saveToStore();
    }

    public void purge() {
        LOGGER.log(Level.INFO, "Purging all devices");
        devices.clear();
        saveToStore();
    }

}
