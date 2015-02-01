package project.core.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by Marta on 2015-01-31.
 */
public class FolderNotFoundException extends WebApplicationException {
    public FolderNotFoundException(Integer folderId){
        super(Response.status(Response.Status.NOT_FOUND)
                .entity("The folder with id "+folderId+" does not exist")
                .build());
    }
}
