import client.WebTargetBuilder;
import org.glassfish.jersey.media.multipart.*;
import org.junit.Before;
import org.junit.Test;
import project.core.dataClasses.SingleFile;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by Okruszon on 2015-02-01.
 */
public class FileResourceTest {

    private WebTarget fileUrl;
    private static final GenericType<List<SingleFile>> FILE_LIST_ENTITY_TYPE = new GenericType<List<SingleFile>>() {
    };

    @Before
    public void setUp() {
        fileUrl = WebTargetBuilder.newUserAuthorizedTarget().getFile();
    }

    //file/upload

    @Test
    public void getUploadShouldNotBeAllowed() {
        //when
        final Response response = fileUrl.path("upload").request().get();
        //then
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void postUploadShouldReturnCreated() {
        byte[] content = new byte[4 * 1024];
        new Random().nextBytes(content);
        FormDataMultiPart multiPart = new FormDataMultiPart().field("file", content, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        Response response = fileUrl.path("upload").request().post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA_TYPE));
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void putUploadShouldNotBeAllowed() {
        //when
        final Response response = fileUrl.path("upload").request().put(ResourceUtils.ANY_ENTITY);
        //then
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void deleteUploadShouldNotBeAllowed() {
        //when
        final Response response = fileUrl.path("upload").request().delete();

        //then
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }


    //file/info

    @Test
     public void getInfoShouldReturn200() {
        final Response response = fileUrl.path("info").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void postInfoShouldNotBeAllowed() {
        final Response response = fileUrl.path("info").request().post(ResourceUtils.ANY_ENTITY);
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void putInfoShouldNotBeAllowed() {
        final Response response = fileUrl.path("info").request().put(ResourceUtils.ANY_ENTITY);
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void deleteInfoShouldNotBeAllowed() {
        final Response response = fileUrl.path("info").request().delete();
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void getInfoShouldServeJSON() {
        final Response response = fileUrl.path("info").request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
        final List<SingleFile> files = response.readEntity(FILE_LIST_ENTITY_TYPE);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
        //assertEquals(0, files.size());
    }

    @Test
    public void getShouldServeXML() {
        final Response response = fileUrl.path("info").request()
                .accept(MediaType.APPLICATION_XML)
                .get();
        final List<SingleFile> files = response.readEntity(FILE_LIST_ENTITY_TYPE);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(MediaType.APPLICATION_XML_TYPE, response.getMediaType());
        //assertEquals(0, files.size());
    }


    //file/info/{id}

    @Test
    public void getInfoIDShouldReturn200() {
        //add file so that there's anything to check
        byte[] content = new byte[4 * 1024];
        new Random().nextBytes(content);
        FormDataMultiPart multiPart = new FormDataMultiPart().field("file", content, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        fileUrl.path("upload").request().post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA_TYPE));

        final Response response = fileUrl.path("info").path(getFilesCount()).request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void getInfoIDShouldReturnNotFoundIfInvalidId() {
        final Response response = fileUrl.path("info/20").request().get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void putInfoIDShouldNotBeAllowed() {
        final Response response = fileUrl.path("info/1").request().put(ResourceUtils.ANY_ENTITY);
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void postInfoIDShouldNotBeAllowed() {
        final Response response = fileUrl.path("info/1").request().post(ResourceUtils.ANY_ENTITY);
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void deleteInfoIdShouldNotBeAllowed() {
        final Response response = fileUrl.path("info/1").request().delete();
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }


    //file/download/{id}

    @Test
    public void getDownloadIdShouldReturn200() {
        final Response response = fileUrl.path("download").path(getFilesCount()).request().get();
        //final Response response = fileUrl.path("download/1").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void getDownloadIdShouldReturnNotFoundIfInvalidId() {
        final Response response = fileUrl.path("download").path(getFilesCount()+1).request().get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void putDownloadIdShouldNotBeAllowed() {
        final Response response = fileUrl.path("download/1").request().put(ResourceUtils.ANY_ENTITY);
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void postDownloadIdShouldNotBeAllowed() {
        final Response response = fileUrl.path("download/1").request().post(ResourceUtils.ANY_ENTITY);
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void deleteDownloadIdShouldNotBeAllowed() {
        final Response response = fileUrl.path("download/1").request().delete();
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }


    //file/{id}

    @Test
    public void getIDShouldNotBeAllowed() {
        final Response response = fileUrl.path("1").request().get();
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void putIDShouldNotBeAllowed() {
        final Response response = fileUrl.path("1").request().put(ResourceUtils.ANY_ENTITY);
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void postIDShouldNotBeAllowed() {
        final Response response = fileUrl.path("1").request().post(ResourceUtils.ANY_ENTITY);
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void deleteIdShouldReturn200() {
        //add file so that there's anything to delete
        byte[] content = new byte[4 * 1024];
        new Random().nextBytes(content);
        FormDataMultiPart multiPart = new FormDataMultiPart().field("file", content, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        fileUrl.path("upload").request().post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA_TYPE));

        final Response response = fileUrl.path(getFilesCount()).request().delete();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void deleteIdShouldReturnedNotFoundIfInvalidId() {
        final Response response = fileUrl.path(getFilesCount()+1).request().delete();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }


    //file/info;fileName={partOfName}

    @Test
    public void getSearchShouldReturn200() {
        final Response response = fileUrl.path("info;fileName=a").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void putSearchShouldNotBeAllowed() {
        final Response response = fileUrl.path("info;fileName=a").request().put(ResourceUtils.ANY_ENTITY);
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void postSearchShouldNotBeAllowed() {
        final Response response = fileUrl.path("info;fileName=a").request().post(ResourceUtils.ANY_ENTITY);
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }

    @Test
    public void deleteSearchShouldNotBeAllowed() {
        final Response response = fileUrl.path("info;fileName=a").request().delete();
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), response.getStatus());
    }




    private String getFilesCount() {
        final Response r = fileUrl.path("info").request()
                .accept(MediaType.APPLICATION_XML)
                .get();
        final List<SingleFile> fileList = r.readEntity(FILE_LIST_ENTITY_TYPE);
        String id = String.valueOf(fileList.get(fileList.size()-1).getId());
        return id;
    }
}
