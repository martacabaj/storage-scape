package project.core;

import project.core.dataClasses.Folder;
import project.core.dataClasses.SingleFile;

import java.util.Collection;

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
    Folder getOneFolder();
    void deleteFolder(Integer folderId, String user);
    void deleteFile(Integer file, String user);
    void updateFolder(Folder folder, String user);
    long getFreeScape(String user);
    Boolean checkIfFolderExists(int folderId, String user);


}
