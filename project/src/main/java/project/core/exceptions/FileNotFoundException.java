package project.core.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by Marta on 2015-01-31.
 */
public class FileNotFoundException extends WebApplicationException {
    public FileNotFoundException(Integer fileId){
        super(Response.status(Response.Status.NOT_FOUND)
        .entity("The file with id "+fileId+" does not exist")
        .build());
    }
}
