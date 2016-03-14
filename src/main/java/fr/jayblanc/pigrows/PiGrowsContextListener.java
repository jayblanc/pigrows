package fr.jayblanc.pigrows;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import fr.jayblanc.pigrows.service.LocalFileDeviceService;

public class PiGrowsContextListener implements ServletContextListener {
    
    private static Logger LOGGER = Logger.getLogger(PiGrowsContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent ctx) {
        LOGGER.log(Level.INFO, "Context Initilized");
        PiGrowsConfig.getInstance();
        LocalFileDeviceService.getInstance();
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent ctx) {
        LOGGER.log(Level.INFO, "Context Destroyed");
        PiGrowsConfig.getInstance().shutdown();
    }

}