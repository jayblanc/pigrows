package fr.jayblanc.pigrows.resources;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.jayblanc.pigrows.model.Event;

@Path("/events")
public class EventResource {
    
    private static Logger LOGGER = Logger.getLogger(EventResource.class.getName());
    
    @Context
    private ServletContext ctx;
    
    @POST
    @Path("/{key}")
    @Consumes({ MediaType.APPLICATION_JSON })
    public void create(@PathParam("key") String key, Event event) throws Exception {
        LOGGER.log(Level.INFO, "Event received: \r\n" + event);
        
    }
    
}
