package project.resources;

import org.glassfish.jersey.media.multipart.FormDataParam;
import project.core.dataClasses.Folder;
import project.core.StorageService;
import project.core.dataClasses.User;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Set;

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


  /*  @PUT
    @Path("/share/{id}")
    @Consumes({"application/xml", "application/json"})
    public Response shareFolder(@PathParam("id")Integer folderId, @Context SecurityContext sc, Set<User> sharedUsers, @Context UriInfo uriInfo,
                              @Context Request request){
        storageService.shareFolder(folderId, sc.getUserPrincipal().getName(), sharedUsers);
        return Response.created(
                UriBuilder.fromUri(uriInfo.getRequestUri())
                        .path(folderId.toString())
                        .build())
                .build();
    }*/
}
