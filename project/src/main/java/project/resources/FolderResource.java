package project.resources;

import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;
import project.core.dataClasses.Folder;
import project.core.StorageService;
import project.core.dataClasses.SingleFile;
import project.core.dataClasses.User;
import project.core.exceptions.FileNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;
import java.util.Set;

/**
 * Created by Marta on 2015-01-03.
 */
@Path("folder")
public class FolderResource {
    private final StorageService storageService;

    public FolderResource(StorageService storageService) { this.storageService = storageService; }

    @POST
    @Consumes({"application/xml", "application/json"})
    public Response addFolder(@Context UriInfo uriInfo,
            @Context SecurityContext sc, Folder folder) {
        Integer id;
        id = storageService.addFolder(folder, sc.getUserPrincipal().getName());
        return Response.created(
                UriBuilder.fromUri(uriInfo.getRequestUri())
                        .path(id.toString())
                        .build())
                .build();
    }
    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Response getFolder(@PathParam("id") int id, @Context SecurityContext sc, @Context Request request) {
        final Folder folder = storageService.getFolder(id, sc.getUserPrincipal().getName());
        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(10);
        EntityTag entityTag = new EntityTag(computeTag(folder));
        final Response.ResponseBuilder responseBuilder = request.evaluatePreconditions(entityTag);
        if (null != responseBuilder) {
            return responseBuilder.status(Response.Status.NOT_MODIFIED)
                    .cacheControl(cacheControl)
                    .tag(entityTag)
                    .build();
        }
        return Response.ok(folder).cacheControl(cacheControl).tag(entityTag).build();
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public Response getFolders(@Context SecurityContext sc, @Context Request request, @MatrixParam("name") String name) {
        Collection<Folder> folders;
        if (StringUtils.isEmpty(name)) {
            folders=storageService.getFolders(sc.getUserPrincipal().getName());
        } else{
            folders=storageService.getFoldersFilteredBy(sc.getUserPrincipal().getName(), name);
        }
        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(10);
        EntityTag entityTag = new EntityTag(computeTag(folders));
        final Response.ResponseBuilder responseBuilder = request.evaluatePreconditions(entityTag);
        if (null != responseBuilder) {
            return responseBuilder.status(Response.Status.NOT_MODIFIED)
                    .cacheControl(cacheControl)
                    .tag(entityTag)
                    .build();
        }
        return Response.ok().entity(new GenericEntity<Collection<Folder>>(folders){}).cacheControl(cacheControl).tag(entityTag).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteFolder(@Context UriInfo uriInfo,@PathParam("id") Integer id, @Context SecurityContext sc) {
        try {
            storageService.deleteFolder(id, sc.getUserPrincipal().getName());
        } catch (Exception e) {
            throw new FileNotFoundException(id);
        }
        return Response.ok().build();
    }
    private String computeTag(Folder folder) {
        return folder.hashCode() + "";
    }
    private String computeTag(Collection<Folder> folders) {
        return folders.hashCode() + "";
    }

}
