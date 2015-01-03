package project.core;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import edu.emory.mathcs.backport.java.util.Collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/**
 * Created by Marta on 2015-01-03.
 */


public class InMemoryStorage implements StorageInterface {
    private final AtomicInteger idCounterFile;
    private final AtomicInteger idCounterFolder;
    private final ConcurrentMap<Integer, SingleFile> files;
    private final ConcurrentMap<Integer, Folder> folders;

    public InMemoryStorage() {
        files = new ConcurrentHashMap<>();
        folders = new ConcurrentHashMap<>();
        idCounterFile = new AtomicInteger(0);
        idCounterFolder=new AtomicInteger(0);

    }

    @Override
    public Integer addFile(SingleFile singleFile) {
      Integer id = idCounterFile.incrementAndGet();
        singleFile.setId(id);
        files.putIfAbsent(id, singleFile);
        return id;
    }

    @Override
    public Integer addFile(SingleFile singleFile, int folderId) {
        Integer id = idCounterFile.incrementAndGet();
        singleFile.setId(id);
        singleFile.setFolderId(folderId);
        files.putIfAbsent(id, singleFile);
        return id;
    }

    @Override
    public Integer addFolder(Folder folder) {
        Integer id = idCounterFile.incrementAndGet();
        folder.setId(id);

        folders.putIfAbsent(id, folder);
        return id;
    }

    @Override
    public Collection<SingleFile> getAllFiles() {
       return files.values();
    }

    @Override
    public Collection<SingleFile> getAllFilesFromFolder(int folderId) {
       if(!folders.containsKey(folderId)){
           throw new IllegalArgumentException("Folder does not exist");
       }
        if(files.isEmpty()){
            return Collections.emptySet();
        }
        Set<SingleFile> filesFromFolder = new HashSet<>();
        for(SingleFile file : files.values()){
            if(file.getFolderId()==folderId){
                filesFromFolder.add(file);
            }
        }
        return filesFromFolder;
    }

    @Override
    public Collection<Folder> getAllFolders() {
        return folders.values();
    }

    @Override
    public SingleFile getOneFile(Integer id) {
        return files.get(id);
    }

    @Override
    public Folder getOneFolder() {
        return null;
    }

    @Override
    public void deleteFolder(Integer folderId) {
        folders.remove(folderId);
        for(SingleFile file : files.values()){
            if(file.getFolderId()==folderId){
               files.remove(file.id);
            }
        }
    }

    @Override
    public void deleteFile(Integer file) {
        files.remove(file);
    }

    @Override
    public void updateFolder(Folder folder) {
       if(null== folder.getId()){
           throw new IllegalArgumentException("Folder has no id");
       }
        folders.put(folder.getId(), folder);
    }
}
