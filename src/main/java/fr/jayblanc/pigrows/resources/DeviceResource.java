package fr.jayblanc.pigrows.resources;

import java.net.URI;
import java.util.Collection;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.server.mvc.Template;

import fr.jayblanc.pigrows.model.Device;
import fr.jayblanc.pigrows.service.DeviceAlreadyExistsException;
import fr.jayblanc.pigrows.service.DeviceNotFoundException;
import fr.jayblanc.pigrows.service.DeviceService;
import fr.jayblanc.pigrows.service.LocalFileDeviceService;

@Path("/devices")
public class DeviceResource {
    
    private static DeviceService service = LocalFileDeviceService.getInstance();
    private static Random rand = new Random();
    
    @Context
    private ServletContext ctx;
    @Context
    private UriInfo uriInfo;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Device> list() {
        return service.listAll();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Device device) throws DeviceAlreadyExistsException {
        String key = Integer.toHexString(rand.nextInt(16777215));
        service.create(key, device.getName(), device.getDescription());
        URI location = uriInfo.getRequestUriBuilder().path(DeviceResource.class).path(key).build();
        return Response.seeOther(location).build();
    }
    
    @GET
    @Path("/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("key") String key) {
        try {
            Device device = service.get(key);
            return Response.ok(device).build();
        } catch (DeviceNotFoundException e) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
    
    @PUT
    @Path("/{key}")
    @Template(name="/devices.ftl")
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(@PathParam(value="key") String key, Device device) throws DeviceNotFoundException {
        service.update(key, device.getName(), device.getDescription());
    }
    
    @DELETE
    @Path("/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam(value="key") String key) throws DeviceNotFoundException {
        service.delete(key);
    }
    
}
