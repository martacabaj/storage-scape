package project.resources;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import project.core.SingleFile;
import project.core.StorageService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Marta on 2015-01-02.
 */
@Path("file")
public class FileResource {
    private final StorageService storageService;

    public FileResource(StorageService storageService) {
        this.storageService = storageService;
    }
//    public FileResource(){
//
//    }

    /*public FileResource() {
    }*/

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadFile(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDisposition,
            @FormDataParam("name") String name) {
if(fileInputStream==null){
    throw new WebApplicationException(Response.status(Response.Status.NOT_ACCEPTABLE)
            .entity("No file")
            .build());
}
        String fileName = fileDisposition.getFileName();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read = 0;
        final byte[] bytes = new byte[1024];
        try {
            while ((read = fileInputStream.read(bytes)) != -1) {
                buffer.write(bytes, 0, read);
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final SingleFile file = new SingleFile(fileName, buffer.toByteArray(), name);
        //FileSingleton.INSTANCE.create(file);
        Integer id;
        try {
            id = storageService.addFile(file);
        } catch (IllegalArgumentException e) {
            throw new WebApplicationException(Response.status(Response.Status.REQUEST_ENTITY_TOO_LARGE)
                    .entity(e.getMessage())
                    .build());
        }
        return Response.ok(id).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    @Path("{id}/{user}")
    public Response downloadFile(@PathParam("id") Integer id, @PathParam("user") String user) {

        try {
            SingleFile singleFile = storageService.getFile(id, user);
            // System.out.println(singleFile.getSize());
            return Response
                    .ok(singleFile.getFileBytesArray(), MediaType.APPLICATION_OCTET_STREAM)
                    .header("content-disposition", "attachment; filename = " + singleFile.getName())
                    .build();
        } catch (NullPointerException e) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build());
        }
    }

    @DELETE
    @Path("{id}/{user}")
    public Response deleteFile(@PathParam("id") Integer id, @PathParam("user") String user) {
        try {
            storageService.deleteFile(id, user);
        } catch (Exception e) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build());
        }
        return Response.ok().build();
    }

}

