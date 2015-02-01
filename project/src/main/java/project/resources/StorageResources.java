package project.resources;

import project.core.StorageService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


/**
 * Created by Marta on 2015-02-01.
 */
@Path("storage")
public class StorageResources {
    private final StorageService storageService;
    public StorageResources(StorageService storageService) { this.storageService = storageService; }
    @GET
    @Path("free")
    @Produces({"text/plain"})
    public Response getFreeSpace(@Context SecurityContext sc) {
        String user = sc.getUserPrincipal().getName();
        long space = storageService.getFreeSpace(user);

        return Response.ok(space + "").build();
    }

}
