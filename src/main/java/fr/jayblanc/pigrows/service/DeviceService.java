package fr.jayblanc.pigrows.service;

import java.util.Collection;

import fr.jayblanc.pigrows.model.Device;

public interface DeviceService {
    
    Collection<Device> listAll();
    
    void create(String key, String name, String description) throws DeviceAlreadyExistsException;
    
    Device get(String key) throws DeviceNotFoundException;
    
    void update(String key, String name, String description) throws DeviceNotFoundException;
    
    void updateActivity(String key) throws DeviceNotFoundException;

    void delete(String key) throws DeviceNotFoundException;
    
}
