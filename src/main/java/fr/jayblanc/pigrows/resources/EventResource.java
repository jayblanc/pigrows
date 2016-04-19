package fr.jayblanc.pigrows.resources;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

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
    
    @DELETE
    @Produces({ MediaType.APPLICATION_JSON })
    public Response purgeAll() throws IOException {
        service.purge();
        return Response.ok().build();
    }
    
    @GET
    @Path("/file")
    public Response file(@DefaultValue("true") @QueryParam("fd") boolean download) throws IOException {
        ResponseBuilder builder = Response.ok();
        if (download) {
            builder.header("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode("events.log.txt", "utf-8"));
        } else {
            builder.header("Content-Disposition", "filename*=UTF-8''" + URLEncoder.encode("events.log.txt", "utf-8"));
        }
        builder.type("text/plain");
        File events = service.readAll().toFile();
        builder.entity(events);
        return builder.build();
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
