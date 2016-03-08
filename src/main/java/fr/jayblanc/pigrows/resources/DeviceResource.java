package fr.jayblanc.pigrows.resources;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Template;

import fr.jayblanc.pigrows.model.Device;
import fr.jayblanc.pigrows.service.DeviceService;

@Path("/")
public class DeviceResource {
    
    @Inject
    private DeviceService service;
    
    @GET
    @Template(name="/devices.ftl")
    @Produces(MediaType.TEXT_HTML)
    public List<Device> list() {
        return service.listAll();
    }

}
