package project.core;

import project.core.dataClasses.Folder;
import project.core.dataClasses.SingleFile;
import project.core.dataClasses.User;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Marta on 2015-01-03.
 */
public interface StorageInterface {
    Integer addFile(SingleFile singleFile);
    Integer addFile(SingleFile singleFile, int folderId);
    Integer addFolder(Folder folder);
    Collection<SingleFile> getAllFiles( String user);
    Collection<SingleFile> getAllFilesFromFolder(int folderId, String user);
    Collection<Folder> getAllFolders( String user);
    SingleFile getOneFile(Integer id, String user);
    Folder getOneFolder(Integer id, String user);
    void deleteFolder(Integer folderId, String user);
    void deleteFile(Integer file, String user);
    void updateFolder(Folder folder, String user);
    long getFreeScape(String user);
    Boolean checkIfFolderExists(int folderId, String user);
    void shareFile(Integer fileId, String owner, Set<User> users);
    void moveFile(SingleFile file, String user);
}
