package project.core;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import edu.emory.mathcs.backport.java.util.Collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Marta on 2015-01-03.
 */


public class InMemoryStorage implements StorageInterface {
    private final AtomicInteger idCounterFile;
    private final AtomicInteger idCounterFolder;
    private final ConcurrentMap<Integer, SingleFile> files;
    private final ConcurrentMap<Integer, Folder> folders;
    private final static long SPACE_LIMIT = 10 * 1024 * 1024;

    public InMemoryStorage() {
        files = new ConcurrentHashMap<>();
        folders = new ConcurrentHashMap<>();
        idCounterFile = new AtomicInteger(0);
        idCounterFolder = new AtomicInteger(0);

    }

    @Override
    public Integer addFile(SingleFile singleFile) {
        long freeSpace =getFreeScape(singleFile.getOwner());
        if(freeSpace<singleFile.getSize()){
            return null;
        }
        Integer id = idCounterFile.incrementAndGet();
        singleFile.setId(id);
        files.putIfAbsent(id, singleFile);
        return id;
    }

    @Override
    public Integer addFile(SingleFile singleFile, int folderId) {
        long freeSpace =getFreeScape(singleFile.getOwner());
        if(freeSpace<singleFile.getSize()){
            return null;
        }
        Integer id = idCounterFile.incrementAndGet();
        singleFile.setId(id);
        singleFile.setFolderId(folderId);
        files.putIfAbsent(id, singleFile);
        return id;
    }

    @Override
    public Integer addFolder(Folder folder) {
        Integer id = idCounterFolder.incrementAndGet();
        folder.setId(id);

        folders.putIfAbsent(id, folder);
        return id;
    }

    @Override
    public Collection<SingleFile> getAllFiles( String user) {
        Set<SingleFile> filesToReturn = new HashSet<SingleFile>();
        for(SingleFile file : files.values()){
            String owner = file.getOwner();
            if (owner.equals(user)){
                filesToReturn.add(file);
            }
        }
        return filesToReturn;
    }

    @Override
    public Collection<SingleFile> getAllFilesFromFolder(int folderId,  String user) {
        if (!folders.containsKey(folderId)) {
            throw new IllegalArgumentException("Folder does not exist");
        }
        if (files.isEmpty()) {
            return Collections.emptySet();
        }
        Set<SingleFile> filesFromFolder = new HashSet<>();
        for (SingleFile file : files.values()) {
            String owner = file.getOwner();

            if (file.getFolderId() == folderId &&owner.equals(user) ) {
                filesFromFolder.add(file);
            }
        }
        return filesFromFolder;
    }

    @Override
    public Collection<Folder> getAllFolders( String user) {
        Set<Folder> foldersToReturn= new HashSet<Folder>();

        for(Folder folder : folders.values()){
            String owner = folder.getOwner();
            if (owner.equals(user)){
                foldersToReturn.add(folder);
            }
        }

        return foldersToReturn;
    }

    @Override
    public SingleFile getOneFile(Integer id, String user) {
        String owner = files.get(id).getOwner();
       if (owner.equals(user))
            return files.get(id);
        else
        return null;

    }

    @Override
    public Folder getOneFolder() {
        return null;
    }

    @Override
    public void deleteFolder(Integer folderId, String user) {
        String owner = files.get(folderId).getOwner();
        if (owner.equals(user)){
            folders.remove(folderId);
        }
        else{
            throw new IllegalArgumentException("User has no folder with this id");
        }

        for (SingleFile file : files.values()) {
            if (file.getFolderId() == folderId && file.getOwner() == user) {
                files.remove(file.id);
            }
        }
    }

    @Override
    public void deleteFile(Integer file, String user) {
        String owner = files.get(file).getOwner();
        if (owner.equals(user)){
            files.remove(file);
        return;
        }else {
           throw new IllegalArgumentException("User has no file with this id");
        }
    }

    @Override
    public void updateFolder(Folder folder, String user) {
        if (null == folder.getId()) {
            throw new IllegalArgumentException("Folder has no id");
        }else if(folder.getOwner()==user){
            throw new IllegalArgumentException("User has no folder with this id");
        }
        folders.put(folder.getId(), folder);
    }

    @Override
    public long getFreeScape(String user) {
        long used = 0;
        for (SingleFile file : files.values()) {
            if (file.getOwner() == user) {
                used += file.getSize();
            }
        }
        return SPACE_LIMIT - used;
    }
}
