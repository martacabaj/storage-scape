package project.core;

import jersey.repackaged.com.google.common.base.Predicate;
import jersey.repackaged.com.google.common.base.Predicates;
import jersey.repackaged.com.google.common.collect.Collections2;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * Created by Marta on 2015-01-01.
 */
public class StorageService {

    private final StorageInterface storage;

    public StorageService(StorageInterface storage) {
        this.storage = storage;
    }

    public SingleFile readFile(InputStream fileInputStream,
                               FormDataContentDisposition fileDisposition,
                               String name) {
        if (fileInputStream == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity("No file")
                    .build());
        }
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

        return new SingleFile(fileName, buffer.toByteArray(), name);

    }

    public Integer addFile(SingleFile singleFile) {
        if (null != singleFile.getId()) {
            throw new IllegalArgumentException("Cannot add file with ID already defined");
        }
        try {
            Integer id = storage.addFile(singleFile);
            return id;
        } catch (NoFreeSpaceException e) {
            throw e;
        }


    }


    public Integer addFile(SingleFile singleFile, Integer folderId) {
        if (null != singleFile.getId()) {
            throw new IllegalArgumentException("Cannot add file with ID already defined");
        }
        try {
            Integer id = storage.addFile(singleFile, folderId);
            return id;
        } catch (NoFreeSpaceException e) {
            throw e;
        }


    }

    public SingleFile getFile(Integer id, String user) {
        SingleFile file = storage.getOneFile(id, user);
        if (file == null) {

            throw new NullPointerException("No file with given id");
        }
        return file;
    }

    public Collection<SingleFile> getFilesFilteredBy(final String user,
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

    public Collection<SingleFile> getFiles(String user) {
        Collection<SingleFile> files = storage.getAllFiles(user);

        return files;
    }

    public Collection<SingleFile> getFilesFromFolder(Integer id, String user) {
        Collection<SingleFile> files = storage.getAllFilesFromFolder(id, user);

        return files;
    }

    public Integer addFolder(Folder folder) {
        if (null != folder.getId()) {
            throw new IllegalArgumentException("Cannot add file with ID already defined");
        }
        Integer id = storage.addFolder(folder);
        return id;
    }

    public void deleteFile(Integer id, String user) {
        try {
            storage.deleteFile(id, user);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public void deleteFolder(Integer id, String user) {
        try {
            storage.deleteFolder(id, user);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public Integer addFileToFolder(SingleFile singleFile, Integer folderId) {
        if(null!= singleFile.getId()){
            throw new IllegalArgumentException("Cannot add file with ID already defined");
        }
        Integer id =storage.addFile(singleFile,folderId);
        if(null == id){
            throw new IllegalArgumentException("No free sapce to add file");
        }
        return id ;
    }
}
