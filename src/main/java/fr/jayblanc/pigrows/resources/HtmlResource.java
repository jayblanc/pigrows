package fr.jayblanc.pigrows.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

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
import javax.ws.rs.core.MultivaluedMap;

import org.glassfish.jersey.server.mvc.Template;

import fr.jayblanc.pigrows.PiGrowsConfig;
import fr.jayblanc.pigrows.PiGrowsConfig.Property;
import fr.jayblanc.pigrows.service.DeviceAlreadyExistsException;
import fr.jayblanc.pigrows.service.DeviceService;
import fr.jayblanc.pigrows.service.EventService;
import fr.jayblanc.pigrows.service.LocalFileDeviceService;
import fr.jayblanc.pigrows.service.LocalFileEventService;
import fr.jayblanc.pigrows.service.LocalFilePictureService;
import fr.jayblanc.pigrows.service.PictureService;

@Path("/html")
public class HtmlResource {

    private static DeviceService service = LocalFileDeviceService.getInstance();
    private static PictureService pictures = LocalFilePictureService.getInstance();
    private static EventService events = LocalFileEventService.getInstance();
    private static PiGrowsConfig config = PiGrowsConfig.getInstance();
    private static Random rand = new Random();

    @Context
    private ServletContext ctx;

    @GET
    @Template(name = "/home.ftl")
    @Produces(MediaType.TEXT_HTML)
    public Map<String, Object> root() {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("context", ctx.getContextPath());
        return model;
    }

    @GET
    @Path("/devices")
    @Template(name = "/devices.ftl")
    @Produces(MediaType.TEXT_HTML)
    public Map<String, Object> listDevices() {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("devices", service.listAll());
        model.put("context", ctx.getContextPath());
        return model;
    }

    @POST
    @Path("/devices")
    @Template(name = "/devices.ftl")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Map<String, Object> createDevice(@FormParam(value = "name") String name, @FormParam(value = "description") String description) {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            String key = Integer.toHexString(rand.nextInt(16777215));
            service.create(key, name, description);
            model.put("msg_success", "Le dispositif a été créé avec la clé: " + key);
        } catch (DeviceAlreadyExistsException e) {
            model.put("msg_error", "Impossible de créer le dispositif : une clé identique existe déjà");
        }
        model.put("devices", service.listAll());
        model.put("context", ctx.getContextPath());
        return model;
    }

    @GET
    @Path("/devices/{key}")
    @Template(name = "/folders.ftl")
    @Produces({ MediaType.TEXT_HTML })
    public Map<String, Object> getDevice(@PathParam("key") String key) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("context", ctx.getContextPath());
        model.put("key", key);
        model.put("folders", pictures.listFolders(key));
        return model;
    }
    
    @POST
    @Path("/devices/{key}/purge")
    @Template(name = "/devices.ftl")
    @Produces({ MediaType.TEXT_HTML })
    public Map<String, Object> purgeDevice(@PathParam("key") String key) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            pictures.purgeAll(key);
            model.put("msg_success", "Toutes les images du dispositif ont &eacute;t&eacute; supprim&eacute;s.");
        } catch ( Exception e) {
            model.put("msg_error", "Impossible de supprimer les images: " + e.getMessage());
        }
        model.put("devices", service.listAll());
        model.put("context", ctx.getContextPath());
        return model;
    }

    @GET
    @Path("/devices/{key}/{folder}")
    @Template(name = "/pictures.ftl")
    @Produces({ MediaType.TEXT_HTML })
    public Map<String, Object> getDeviceFolder(@PathParam("key") String key, @PathParam("folder") String folder, @DefaultValue("1") @QueryParam("p") int page,
            @DefaultValue("15") @QueryParam("size") int size) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("context", ctx.getContextPath());
        model.put("key", key);
        model.put("folder", folder);
        long nbpictures = pictures.count(key, folder);
        model.put("total", nbpictures);
        model.put("nbpages", nbpictures / size + 1);
        model.put("page", page);
        model.put("size", size);
        model.put("pictures", pictures.listPictures(key, folder, (page - 1) * size, size));
        return model;
    }
    
    @POST
    @Path("/devices/{key}/{folder}/purge")
    @Template(name = "/folders.ftl")
    @Produces({ MediaType.TEXT_HTML })
    public Map<String, Object> purgeDeviceFolder(@PathParam("key") String key, @PathParam("folder") String folder, @DefaultValue("1") @QueryParam("p") int page,
            @DefaultValue("15") @QueryParam("size") int size) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            pictures.purgeFolder(key, folder);
            model.put("msg_success", "Toutes les images du dossier ont &eacute;t&eacute; supprim&eacute;s.");
        } catch ( Exception e) {
            model.put("msg_error", "Impossible de supprimer les images: " + e.getMessage());
        }
        model.put("context", ctx.getContextPath());
        model.put("key", key);
        model.put("folders", pictures.listFolders(key));
        return model;
    }

    @GET
    @Path("/events")
    @Template(name = "/events.ftl")
    @Produces({ MediaType.TEXT_HTML })
    public Map<String, Object> listEvents(@QueryParam("key") String key, @DefaultValue("1") @QueryParam("p") int page, @DefaultValue("15") @QueryParam("size") int size) throws IOException {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("context", ctx.getContextPath());
        long nbevents = events.count(key);
        model.put("total", nbevents);
        model.put("nbpages", nbevents / size + 1);
        model.put("page", page);
        model.put("size", size);
        model.put("events", events.find(key, (page-1) * size, size));
        return model;
    }
    
    @POST
    @Path("/events/purge")
    @Template(name = "/events.ftl")
    @Produces({ MediaType.TEXT_HTML })
    public Map<String, Object> purgeEvents(@QueryParam("key") String key, @DefaultValue("1") @QueryParam("p") int page, @DefaultValue("15") @QueryParam("size") int size) throws IOException {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            events.purge();
            model.put("msg_success", "Tous les &eacute;v&egrave;nements ont &eacute;t&eacute; supprim&eacute;s.");
        } catch ( Exception e) {
            model.put("msg_error", "Impossible de supprimer les &eacute;v&egrave;nements: " + e.getMessage());
        }
        model.put("context", ctx.getContextPath());
        long nbevents = events.count(key);
        model.put("total", nbevents);
        model.put("nbpages", nbevents / size + 1);
        model.put("page", page);
        model.put("size", size);
        model.put("events", events.find(key, (page-1) * size, size));
        return model;
    }
    
    @GET
    @Path("/config")
    @Template(name = "/config.ftl")
    @Produces({ MediaType.TEXT_HTML })
    public Map<String, Object> getConfig() throws IOException {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("context", ctx.getContextPath());
        Properties props = config.listProperties();
        for ( String key : props.stringPropertyNames() ) {
            model.put(key, props.get(key));
        }
        return model;
    }
    
    @POST
    @Path("/config")
    @Template(name = "/config.ftl")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({ MediaType.TEXT_HTML })
    public Map<String, Object> setConfig(MultivaluedMap<String, String> params) throws IOException {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            Map<Property, String> properties = new HashMap<Property, String>();
            for ( String paramName : params.keySet() ) {
                properties.put(Property.valueOf(paramName), params.getFirst(paramName));
            }
            config.setProperties(properties);
            model.put("msg_success", "La configuration a &eacute;t&eacute; mise &agrave; jour");
        } catch (Exception e) {
            e.printStackTrace();
            model.put("msg_error", "Impossible de mettre &agrave; jour la configuration: " + e.getMessage());
        }
        model.put("context", ctx.getContextPath());
        Properties props = config.listProperties();
        for ( String key : props.stringPropertyNames() ) {
            model.put(key, props.get(key));
        }
        return model;
    }
    
}
