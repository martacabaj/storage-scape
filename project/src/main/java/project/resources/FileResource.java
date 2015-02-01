package project.resources;

import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import project.core.StorageService;
import project.core.dataClasses.SingleFile;
import project.core.dataClasses.User;
import project.core.exceptions.FileNotFoundException;
import project.core.exceptions.FolderNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.util.Collection;
import java.util.Set;

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
                               @Context SecurityContext sc) {
        if (fileInputStream == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity("No file")
                    .build());
        }
        final SingleFile file = storageService.readFile(fileInputStream, fileDisposition, sc.getUserPrincipal().getName());
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
    public Response uploadFileToFolder(@Context UriInfo uriInfo,
                               @PathParam("id") Integer folderId,
                               @FormDataParam("file") InputStream fileInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDisposition,
                               @Context SecurityContext sc) {
        if (fileInputStream == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity("No file")
                    .build());
        }
        final SingleFile file = storageService.readFile(fileInputStream, fileDisposition, sc.getUserPrincipal().getName());
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
    @Path("download/{id}")
    public Response downloadFile(@PathParam("id") Integer id, @Context SecurityContext sc) {

        try {
            SingleFile singleFile = storageService.getFile(id, sc.getUserPrincipal().getName());

            return Response
                    .ok(singleFile.getFileBytesArray(), MediaType.APPLICATION_OCTET_STREAM)
                    .header("content-disposition", "attachment; filename = " + singleFile.getName())
                    .build();
        } catch (NullPointerException e) {
            throw new FileNotFoundException(id);
        }
    }

    @GET
    @Path("info/{id}")
    @Produces({"application/xml", "application/json"})
    public Response getFile(@PathParam("id") int id, @Context SecurityContext sc, @Context Request request) {
        final SingleFile file = storageService.getFile(id, sc.getUserPrincipal().getName());
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
    @Path("info")
    @Produces({"application/xml", "application/json"})
    public Response getFiles(@Context SecurityContext sc, @Context Request request, @MatrixParam("name") String name) {
        final Response.ResponseBuilder rb = Response.ok();
        String user = sc.getUserPrincipal().getName();
        if (StringUtils.isEmpty(name)) {
            rb.entity(new GenericEntity<Collection<SingleFile>>(storageService.getFiles(user)) {
            });

        } else {
            rb.entity(
                    new GenericEntity<Collection<SingleFile>>(
                            storageService.getFilesFilteredBy(user, name)) {
                    });
        }


        return rb.build();
    }

    @GET
    @Path("folder/{id}")
    @Produces({"application/xml", "application/json"})
    public Response getFileFromFolder(@PathParam("id") Integer id, @Context SecurityContext sc) {
        String user = sc.getUserPrincipal().getName();
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
    @Path("{id}")
    public Response deleteFile(@PathParam("id") Integer id, @Context SecurityContext sc) {
        try {
            storageService.deleteFile(id, sc.getUserPrincipal().getName());
        } catch (Exception e) {
            throw new FileNotFoundException(id);
        }
        return Response.ok().build();
    }

    @PUT
    @Path("/share/{id}")
    @Consumes({"application/xml", "application/json"})
    public Response shareFile(@PathParam("id") Integer fileId, @Context SecurityContext sc, Set<User> sharedUsers, @Context UriInfo uriInfo,
                              @Context Request request) {
        storageService.shareFile(fileId, sc.getUserPrincipal().getName(), sharedUsers);
        return Response.created(
                UriBuilder.fromUri(uriInfo.getRequestUri())
                        .path(fileId.toString())
                        .build())
                .build();
    }

    private String computeTag(SingleFile file) {
        return file.hashCode() + "";
    }

    private String computeTag(Collection<SingleFile> files) {
        return files.hashCode() + "";
    }
}

