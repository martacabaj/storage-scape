import client.WebTargetBuilder;
import org.junit.Before;
import org.junit.Test;
import project.core.dataClasses.Folder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * Created by Maciek on 2015-02-01.
 */
public class StorageResourcesTest {

    public static final Entity<String> TEXT_ENTITY = Entity.entity("Text", MediaType.TEXT_PLAIN);

    private WebTarget storage;

    @Before
    public void setUp() {
        storage = WebTargetBuilder.newUserAuthorizedTarget().getFreeStorage();
    }

    @Test
    public void postShouldNotBeAllowed() {
        final Response response = storage.request().post(TEXT_ENTITY);
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void getShouldReturn200() {
        final Response response = storage.request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void putShouldNotBeAllowed() {
        final Response response = storage.request().put(TEXT_ENTITY);
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void getShouldProduceTextPlain() {
        final Response response = storage.request()
                .accept(MediaType.TEXT_PLAIN)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(MediaType.TEXT_PLAIN_TYPE, response.getMediaType());
    }

    @Test
    public void deleteShouldNotBeAllowed() {
        final Response response = storage.request().delete();
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

}
