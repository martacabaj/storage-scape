package project.core.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by Marta on 2015-01-31.
 */
public class NoFreeSpaceException extends WebApplicationException {
    public NoFreeSpaceException(){
        super(Response.status(Response.Status.NOT_FOUND)
                .entity("No free space available")
                .build());
    }
}
