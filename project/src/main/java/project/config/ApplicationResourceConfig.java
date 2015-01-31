package project.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import project.core.InMemoryStorage;
import project.core.StorageInterface;
import project.core.StorageService;
import project.resources.FileResource;
import project.resources.FolderResource;
import project.resources.StorageResources;

/**
 * Created by Marta on 2015-01-03.
 */
public class ApplicationResourceConfig extends ResourceConfig {

    public ApplicationResourceConfig() {

       StorageInterface storage = new InMemoryStorage();

        StorageService storageService = new StorageService(storage);

        register(new FileResource(storageService));
        register(new FolderResource(storageService));

        register(new StorageResources(storageService));
        // setting a package that contains the REST resource classes
        packages("project.resources");

        //register(RolesAllowedDynamicFeature.class);

    }
}

