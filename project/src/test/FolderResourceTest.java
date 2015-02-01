import client.WebTargetBuilder;
import org.junit.Before;
import org.junit.Test;
import project.core.dataClasses.Folder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.Assert.assertEquals;
/**
 * Created by Maciek on 2015-02-01.
 */
public class FolderResourceTest {

    private static final GenericType<List<Folder>> FOLDER_LIST_ENTITY_TYPE = new GenericType<List<Folder>>() {
    };

    private static final Folder FOLDER_TO_POST = new Folder("folder");
    public static final Entity<String> TEXT_ENTITY = Entity.entity("Text", MediaType.TEXT_PLAIN);

    private WebTarget folders;

    @Before
    public void setUp() {
        folders = WebTargetBuilder.newUserAuthorizedTarget().getFolder();
        folders.request("application/xml").post(Entity.entity(FOLDER_TO_POST, "application/xml"));
    }

    @Test
    public void postShouldReturn201() {
        final Response response = folders.request("application/xml").post(Entity.entity(FOLDER_TO_POST, "application/xml"));
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void postParamShouldNotBeAllowed() {
        String id = getFoldersCount();
        final Response response = folders.path(id).request("application/xml").post(Entity.entity(FOLDER_TO_POST, "application/xml"));
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void getShouldReturn200() {
        final Response response = folders.request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void getParamShouldReturn200() {
        String id = getFoldersCount();
        final Response response = folders.path(id).request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void putShouldNotBeAllowed() {
        final Response response = folders.request().put(TEXT_ENTITY);
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void putParamShouldNotBeAllowed() {
        String id = getFoldersCount();
        final Response response = folders.path(id).request().put(TEXT_ENTITY);
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void getShouldProduceJSON() {
        final Response response = folders.request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
    }

    @Test
    public void getParamShouldProduceJSON() {
        String id = getFoldersCount();
        final Response response = folders.path(id).request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
    }

    @Test
    public void getShouldProduceXML() {
        final Response response = folders.request()
                .accept(MediaType.APPLICATION_XML)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(MediaType.APPLICATION_XML_TYPE, response.getMediaType());
    }

    @Test
    public void getParamShouldProduceXML() {
        String id = getFoldersCount();
        final Response response = folders.path(id).request()
                .accept(MediaType.APPLICATION_XML)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(MediaType.APPLICATION_XML_TYPE, response.getMediaType());
    }

    @Test
    public void deleteShouldNotBeAllowed() {
        final Response response = folders.request().delete();
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void deleteParamShouldReturn200() {
        String id = getFoldersCount();
        final Response response = folders.path(id).request().delete();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    private String getFoldersCount(){
        final Response r = folders.request()
                .accept(MediaType.APPLICATION_XML)
                .get();
        final List<Folder> folderList = r.readEntity(FOLDER_LIST_ENTITY_TYPE);
        String id = String.valueOf(folderList.get(folderList.size()-1).getId());
        return id;
    }
}
