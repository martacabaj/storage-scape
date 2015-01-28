package project.resources;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import project.core.FileSingleton;
import project.core.SingleFile;
import project.core.StorageService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import javax.ws.rs.DELETE;
/**
 * Created by Marta on 2015-01-02.
 */
@Path("/file")
public class FileResource {
  /*  private final StorageService storageService;
    public FileResource(StorageService storageService) {
        this.storageService = storageService;
    }
*/
    /*public FileResource() {
    }*/

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {

        String uploadedFileLocation = "C://" + fileDetail.getFileName();

        writeToFile(uploadedInputStream, uploadedFileLocation);

        String output = "File uploaded to : " + uploadedFileLocation;

        return Response.status(200).entity(output).build();

    }

    private void writeToFile(InputStream uploadedInputStream,
                             String uploadedFileLocation) {

        try {
            OutputStream out = new FileOutputStream(new File(
                    uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    @GET
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    @Path("download")
    public Response downloadFile() {

        final SingleFile singleFile = FileSingleton.INSTANCE.getOne(0);
        System.out.println(singleFile.getSize());
        return Response
                .ok(singleFile.getFileBytesArray(), MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition", "attachment; filename = " + singleFile.getName())
                .build();
    }
    @DELETE
    @Path("{id}")
    public Response deleteFile(@PathParam("id")Integer id){
        //TODO delete file
        return Response.noContent().build();
    }

}

