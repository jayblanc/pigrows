package fr.jayblanc.pigrows.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.jayblanc.pigrows.service.DeviceAlreadyExistsException;
import fr.jayblanc.pigrows.service.LocalFileDeviceService;

public class FileDeviceServiceTest {
    
    @BeforeClass
    public static void setEnv() {
        System.setProperty("PIGROWS_HOME", "/tmp/pigrows");
    }
    
    @AfterClass
    public static void cleanEnv() {
        LocalFileDeviceService.getInstance().purge();
    }
    
    @Test
    public void testDeviceStore() throws DeviceAlreadyExistsException {
        LocalFileDeviceService service = LocalFileDeviceService.getInstance();
        service.create("tagada", "TagadaZone", "A zone to be monitored");
        service.create("tagada2", "TagadaZone2", "A zone to be monitored also");
    }

}
