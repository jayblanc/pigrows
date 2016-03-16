package fr.jayblanc.pigrows.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import fr.jayblanc.pigrows.service.DeviceNotFoundException;
import fr.jayblanc.pigrows.service.DeviceService;
import fr.jayblanc.pigrows.service.LocalFileDeviceService;
import fr.jayblanc.pigrows.service.LocalFilePictureService;
import fr.jayblanc.pigrows.service.PictureService;

@Path("/pictures")
public class PictureResource {

    private static PictureService pictures = LocalFilePictureService.getInstance();
    private static DeviceService devices = LocalFileDeviceService.getInstance();

    @Context
    private ServletContext ctx;

    @POST
    @Path("/{key}")
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    public Response upload(@PathParam("key") String key, @FormDataParam("file") InputStream fileInputStream, @FormDataParam("file") FormDataContentDisposition fileMetaData) throws Exception {
        try {
            devices.get(key);
            pictures.store(key, fileMetaData.getFileName(), fileInputStream);
        } catch (DeviceNotFoundException e) {
            return Response.status(Status.BAD_REQUEST).entity("device key does not exists").build();
        } catch (IOException e) {
            return Response.serverError().build();
        }
        return Response.ok().build();
    }

    @GET
    @Path("/{key}")
    public Response exportAllPictures(@PathParam("key") String key) throws Exception {
        ResponseBuilder builder = Response.ok();
        builder.header("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(key, "utf-8") + ".zip");
        builder.type("application/zip");
        StreamingOutput stream = output -> {
            pictures.exportAll(key, output);
        };
        builder.entity(stream);
        return builder.build();
    }

    @GET
    @Path("/{key}/{folder}")
    public Response getDeviceFolder(@PathParam("key") String key, @PathParam("folder") String folder) throws Exception {
        ResponseBuilder builder = Response.ok();
        builder.header("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(key + "." + folder, "utf-8") + ".zip");
        builder.type("application/zip");
        StreamingOutput stream = output -> {
            pictures.exportFolder(key, folder, output);
        };
        builder.entity(stream);
        return builder.build();
    }

    @GET
    @Path("/{key}/{folder}/{name}")
    public Response getDevicePicture(@PathParam("key") String key, @PathParam("folder") String folder, @PathParam("name") String name, @DefaultValue("false") @QueryParam("fd") boolean download)
            throws Exception {
        ResponseBuilder builder = Response.ok();
        if (download) {
            builder.header("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(name, "utf-8"));
        } else {
            builder.header("Content-Disposition", "filename*=UTF-8''" + URLEncoder.encode(name, "utf-8"));
        }
        builder.type("image/jpeg");
        File picture = pictures.download(key, name);
        builder.entity(picture);
        return builder.build();
    }

}
