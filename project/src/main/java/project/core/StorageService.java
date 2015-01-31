package project.core;

import jersey.repackaged.com.google.common.base.Predicate;
import jersey.repackaged.com.google.common.base.Predicates;
import jersey.repackaged.com.google.common.collect.Collections2;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Marta on 2015-01-01.
 */
public class StorageService {

    private final StorageInterface storage;

    public StorageService(StorageInterface storage) {
        this.storage = storage;
    }

    public Integer addFile(SingleFile singleFile) {
        if(null!= singleFile.getId()){
            throw new IllegalArgumentException("Cannot add file with ID already defined");
        }
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

    public  Collection<SingleFile>  getFilesFilteredBy(final String user,
                                                final String fileName) {
        Predicate<SingleFile> namePredicate = new Predicate<SingleFile>() {
            @Override
            public boolean apply(SingleFile file) {
                return StringUtils.isEmpty(file.getName()) || file.getName().toLowerCase().contains(fileName.toLowerCase());
            }
        };



        return Collections2.filter(storage.getAllFiles(user),
                Predicates.and(namePredicate));
    }

    public  Collection<SingleFile> getFiles(String user){
        Collection<SingleFile> files = storage.getAllFiles(user);

        return files;
    }
    public Integer addFolder (Folder folder){
        if(null!= folder.getId()){
            throw new IllegalArgumentException("Cannot add file with ID already defined");
        }
        Integer id =storage.addFolder(folder);
        return id ;
    }
    public void deleteFile(Integer id, String user){
        try{
            storage.deleteFile(id, user);
        }catch(IllegalArgumentException e){
            throw e;
        }
    }

}
