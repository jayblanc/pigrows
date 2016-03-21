package fr.jayblanc.pigrows.service;

import java.util.Collection;

import javax.xml.bind.ValidationException;

import fr.jayblanc.pigrows.model.Device;
import fr.jayblanc.pigrows.model.CameraConfig;

public interface DeviceService {
    
    Collection<Device> listAll();
    
    void create(String key, String name, String description) throws DeviceAlreadyExistsException;
    
    Device get(String key) throws DeviceNotFoundException;
    
    void update(String key, String name, String description) throws DeviceNotFoundException;
    
    void updateConfig(String key, CameraConfig master, CameraConfig slave) throws DeviceNotFoundException, ValidationException;
    
    void updateActivity(String key) throws DeviceNotFoundException;

    void delete(String key) throws DeviceNotFoundException;
    
}
