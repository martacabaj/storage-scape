package project.core.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by Marta on 2015-01-31.
 */
public class PostWithIdException  extends WebApplicationException {
    public PostWithIdException() {
        super(Response.status(Response.Status.NOT_FOUND)
                .entity("Cannot post object with predefined id")
                .build());
    }
}
