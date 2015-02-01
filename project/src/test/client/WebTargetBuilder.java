package client;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Created by Marta on 2015-02-01.
 */
public class WebTargetBuilder {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWD_CLEARTEXT = "admin123";

    private static final String USER_USERNAME = "user";
    private static final String USER_PASSWD_CLEARTEXT = "user123";

    private static final String NONUSER_USERNAME = "unknown";
    private static final String NONUSER_PASSWD_CLEARTEXT = "unknown123";

    private WebTarget webTarget;

    private WebTargetBuilder(Client client) {
        client.register(MultiPartFeature.class);
        this.webTarget = client.target(ClientUtils.HOST_URL);
    }

    public static WebTargetBuilder newNotAuthenticatedTarget() {
        return new WebTargetBuilder(ClientBuilder.newClient());
    }

    public static WebTargetBuilder newNotAuthorizedTarget() {
        return new WebTargetBuilder(createDigestAuthClient(NONUSER_USERNAME, NONUSER_PASSWD_CLEARTEXT));
    }

    private static Client createDigestAuthClient(String username, String password) {
        return ClientBuilder.newClient()
                .register(HttpAuthenticationFeature.digest(username, password));
    }



    public static WebTargetBuilder newUserAuthorizedTarget() {
        return new WebTargetBuilder(createDigestAuthClient(USER_USERNAME, USER_PASSWD_CLEARTEXT));
    }

    public WebTarget getFolder() {
        return webTarget.path(ClientUtils.FOLDER_PATH);
    }

    public WebTarget getFreeStorage() {
        return webTarget.path(ClientUtils.STORAGE_PATH);
    }

    public WebTarget getFile() {
        return webTarget.path(ClientUtils.FILE_PATH);
    }

    public WebTarget getMultipart() {
        return webTarget.register(MultiPartFeature.class).path(ClientUtils.FILE_PATH).path("upload");
    }
}
