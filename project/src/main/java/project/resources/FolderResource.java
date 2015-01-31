package project.resources;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import project.core.Folder;
import project.core.StorageService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Marta on 2015-01-03.
 */
@Path("folder")
public class FolderResource {
    private final StorageService storageService;

    public FolderResource(StorageService storageService) { this.storageService = storageService; }

    @POST
    @Path("add")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addFolder(
            @FormDataParam("name") String name,
            @FormDataParam("owner") String owner) {

        final Folder folder = new Folder(name, owner);
        Integer id = 1;
        try {
            id = storageService.addFolder(folder);
        } catch (IllegalArgumentException e) {
            throw new WebApplicationException(javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.REQUEST_ENTITY_TOO_LARGE)
                    .entity(e.getMessage())
                    .build());
        }
        return javax.ws.rs.core.Response.ok(id).build();
    }
}
