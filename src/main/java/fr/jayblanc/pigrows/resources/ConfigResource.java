package fr.jayblanc.pigrows.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;

import fr.jayblanc.pigrows.PiGrowsConfig;
import fr.jayblanc.pigrows.model.Device;
import fr.jayblanc.pigrows.model.PictureParams;
import fr.jayblanc.pigrows.service.DeviceNotFoundException;
import fr.jayblanc.pigrows.service.DeviceService;
import fr.jayblanc.pigrows.service.LocalFileDeviceService;

@Path("/config")
public class ConfigResource {

    private static final PiGrowsConfig config = PiGrowsConfig.getInstance();
    private static final DeviceService devices = LocalFileDeviceService.getInstance();
    
    @Context
    private ServletContext ctx;
    
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Map<String, String> getConfig() throws IOException {
        Map<String, String> sendback = new HashMap<String, String> ();
        Properties props = config.listProperties();
        for ( String key : props.stringPropertyNames() ) {
            sendback.put(key, props.getProperty(key));
        }
        return sendback;
    }
    
    @GET
    @Path("/{key}/{camera}")
    @Produces({ MediaType.APPLICATION_JSON })
    public PictureParams getPictureParams(@PathParam("key") String key, @PathParam("camera") String camera) throws DeviceNotFoundException, NoContentException {
        Device device = devices.get(key);
        if ( camera.equals("master") ) {
            return device.getMasterPictureConfig();
        }
        if ( camera.equals("slave") ) {
            return device.getSlavePictureConfig();
        }
        throw new NoContentException("no camera found with name: " + camera);
    }
}
