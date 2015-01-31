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
    public Response uploadFile(@Context UriInfo uriInfo,
                               @FormDataParam("file") InputStream fileInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDisposition,
                               @FormDataParam("name") String name) {
        final SingleFile file = storageService.readFile(fileInputStream, fileDisposition, name);
        Integer id;
        id = storageService.addFile(file);

        return Response.created(
                UriBuilder.fromUri(uriInfo.getRequestUri())
                        .path(id.toString())
                        .build())
                .build();
    }

    @POST
    @Path("upload/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadFile(@Context UriInfo uriInfo,
                               @PathParam("id") Integer folderId,
                               @FormDataParam("file") InputStream fileInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDisposition,
                               @FormDataParam("name") String name) {
        final SingleFile file = storageService.readFile(fileInputStream, fileDisposition, name);
        Integer id;
        id = storageService.addFile(file, folderId);

        return Response.created(
                UriBuilder.fromUri(uriInfo.getRequestUri())
                        .path(id.toString())
                        .build())
                .build();
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
    public Response getFiles(@PathParam("user") String user, @Context Request request, @MatrixParam("fileName") String fileName) {
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
    public Response getFileFromFolder(@PathParam("id") Integer id, @PathParam("user") String user) {
        Collection<SingleFile> files = storageService.getFilesFromFolder(id, user);
        if (files == null) {
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

