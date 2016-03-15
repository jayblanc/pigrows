package fr.jayblanc.pigrows.resources;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import fr.jayblanc.pigrows.model.Event;
import fr.jayblanc.pigrows.service.LocalFileEventService;

@Path("/events")
public class EventResource {
    
    private LocalFileEventService service = LocalFileEventService.getInstance();
    
    @Context
    private ServletContext ctx;
    
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Event> listAll(@DefaultValue("0") @QueryParam("offset") int offset, @DefaultValue("1000") @QueryParam("limit") int limit) throws IOException {
        return service.find(null, offset, limit);
    }
    
    @GET
    @Path("/{key}")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Event> listForDevice(@PathParam("key") String key, @DefaultValue("0") @QueryParam("offset") int offset, @DefaultValue("1000") @QueryParam("limit") int limit) throws IOException {
        return service.find(key, offset, limit);
    }
    @POST
    @Path("/{key}")
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    public void appendForm(@PathParam("key") String key, @FormParam("type") String type, @FormParam("message") String message) throws IOException {
        service.append(key, type, message);
    }
    
    @POST
    @Path("/{key}")
    public void appendParams(@PathParam("key") String key, @QueryParam("type") String type, @QueryParam("message") String message) throws IOException {
        service.append(key, type, message);
    }
    
    
    
    
}