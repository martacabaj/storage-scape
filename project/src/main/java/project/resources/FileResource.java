package project.resources;

import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import project.core.FileNotFoundException;
import project.core.FolderNotFoundException;
import project.core.SingleFile;
import project.core.StorageService;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * Created by Marta on 2015-01-02.
 */
@Path("file")
public class FileResource {
    private final StorageService storageService;

    public FileResource(StorageService storageService) {
        this.storageService = storageService;
    }


    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadFile(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDisposition,
            @FormDataParam("name") String name) {
        if (fileInputStream == null) {
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
    @Path("download/{id}/{user}")
    public Response downloadFile(@PathParam("id") Integer id, @PathParam("user") String user) {

        try {
            SingleFile singleFile = storageService.getFile(id, user);

            return Response
                    .ok(singleFile.getFileBytesArray(), MediaType.APPLICATION_OCTET_STREAM)
                    .header("content-disposition", "attachment; filename = " + singleFile.getName())
                    .build();
        } catch (NullPointerException e) {
            throw new FileNotFoundException(id);
        }
    }

    @GET
    @Path("info/{id}/{user}")
    @Produces({"application/xml", "application/json"})
    public Response getFile(@PathParam("id") int id, @PathParam("user") String user, @Context Request request) {
        final SingleFile file = storageService.getFile(id, user);
        if (file == null) {
            throw new FileNotFoundException(id);
        }
        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(10);
        EntityTag entityTag = new EntityTag(computeTag(file));
        final Response.ResponseBuilder responseBuilder = request.evaluatePreconditions(entityTag);
        if (null != responseBuilder) {
            return responseBuilder.status(Response.Status.NOT_MODIFIED)
                    .cacheControl(cacheControl)
                    .tag(entityTag)
                    .build();
        }
        return Response.ok(file).cacheControl(cacheControl).tag(entityTag).build();
    }

    @GET
      @Path("info/{user}")
      @Produces({"application/xml", "application/json"})
      public Response getFiles(@PathParam("user") String user, @Context Request request,  @MatrixParam("fileName") String fileName) {
        final Response.ResponseBuilder rb = Response.ok();

        if (StringUtils.isEmpty(fileName)) {
            rb.entity(new GenericEntity<Collection<SingleFile>>(storageService.getFiles(user)) {
            });

        } else {
            rb.entity(
                    new GenericEntity<Collection<SingleFile>>(
                            storageService.getFilesFilteredBy(user, fileName)) {
                    });
        }


        return rb.build();
    }

    @GET
    @Path("folder/{id}/{user}")
    @Produces({"application/xml", "application/json"})
    public Response getFileFromFolder(@PathParam("id") Integer id,@PathParam("user") String user) {
        Collection<SingleFile> files = storageService.getFilesFromFolder(id, user);
        if(files ==null){
            throw new FolderNotFoundException(id);
        }
        final Response.ResponseBuilder rb = Response.ok();
            rb.entity(new GenericEntity<Collection<SingleFile>>(files) {
            });
        return rb.build();
    }


    @DELETE
    @Path("{id}/{user}")
    public Response deleteFile(@PathParam("id") Integer id, @PathParam("user") String user) {
        try {
            storageService.deleteFile(id, user);
        } catch (Exception e) {
            throw new FileNotFoundException(id);
        }
        return Response.ok().build();
    }

    private String computeTag(SingleFile file) {
        return file.hashCode() + "";
    }
    private String computeTag(Collection<SingleFile> files) {
        return files.hashCode() + "";
    }
}

