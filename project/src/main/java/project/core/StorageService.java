package project.core;

import java.util.Collection;

/**
 * Created by Marta on 2015-01-01.
 */
public class StorageService {

    private final StorageInterface storage;

    public StorageService(StorageInterface storage) {
        this.storage = storage;
    }
public Integer addFile(SingleFile singleFile){
return storage.addFile(singleFile);
}

}
