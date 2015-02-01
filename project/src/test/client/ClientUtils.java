package client;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Created by Marta on 2015-02-01.
 */
public class ClientUtils {
    public static final String HOST_URL = "http://localhost:8080/webapi";
    public static final String STORAGE_PATH = "storage/free";
    public static final String FILE_PATH = "file";
    public static final String FOLDER_PATH= "folder";
    public static final String FOLDER_PARAM_PATH = "folder/1";

    private ClientUtils() {
    }

}
