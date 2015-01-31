package project.resources;

import org.glassfish.jersey.media.multipart.FormDataParam;
import project.core.dataClasses.Folder;
import project.core.StorageService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

    @DELETE
    @Path("{id}/{user}")
    public Response deleteFolder(@PathParam("id") Integer id, @PathParam("user") String user) {
        try {
            storageService.deleteFolder(id, user);
        } catch (Exception e) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build());
        }
        return Response.ok().build();
    }
}
