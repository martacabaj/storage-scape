package project.resources;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import project.core.FileSingleton;
import project.core.SingleFile;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Marta on 2015-01-02.
 */
@Path("file")
public class FileResource {

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public String uploadFile(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDisposition) {

        String fileName = fileDisposition.getFileName();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read = 0;
        final byte[] bytes = new byte[1024];
        try {
            while ((read = fileInputStream.read(bytes)) != -1) {
                buffer.write(bytes, 0, read);
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final SingleFile file = new SingleFile(fileName, buffer.toByteArray());
        FileSingleton.INSTANCE.create(file);

        return "File Upload Successfully !!";
    }

    @GET
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    @Path("download")
    public Response downloadFile() {

        final SingleFile singleFile = FileSingleton.INSTANCE.getOne(0);
        System.out.println(singleFile.getSize());
        return Response
                .ok(singleFile.getFileBytes(), MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition", "attachment; filename = " + singleFile.getName())
                .build();
    }
}

