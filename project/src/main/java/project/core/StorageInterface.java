package project.core;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;

import java.util.Collection;

/**
 * Created by Marta on 2015-01-03.
 */
public interface StorageInterface {
    Integer addFile(SingleFile singleFile);
    Integer addFile(SingleFile singleFile, int folderId);
    Integer addFolder(Folder folder);
    Collection<SingleFile> getAllFiles();
    Collection<SingleFile> getAllFilesFromFolder(int folderId);
    Collection<Folder> getAllFolders();
    SingleFile getOneFile(Integer id);
    Folder getOneFolder();
    void deleteFolder(Integer folderId);
    void deleteFile(Integer file);
    void updateFolder(Folder folder);

}
