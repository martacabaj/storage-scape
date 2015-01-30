package project.core;

/**
 * Created by Marta on 2015-01-01.
 */
public class StorageService {

    private final StorageInterface storage;

    public StorageService(StorageInterface storage) {
        this.storage = storage;
    }

    public Integer addFile(SingleFile singleFile) {
        Integer id =storage.addFile(singleFile);
        if(null == id){
            throw new IllegalArgumentException("No free sapce to add file");
        }
        return id ;
    }
    public SingleFile getFile(Integer id, String user){
        SingleFile file = storage.getOneFile(id, user);
        if(file==null){

            throw new NullPointerException("No file with given id");
        }
        return file;
    }
    public Integer addFolder (Folder folder){
        return storage.addFolder(folder);
    }
    public void deleteFile(Integer id, String user){
        try{
            storage.deleteFile(id, user);
        }catch(IllegalArgumentException e){
            throw e;
        }
    }

}
