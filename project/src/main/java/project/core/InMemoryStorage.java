package project.core;

import edu.emory.mathcs.backport.java.util.Collections;
import project.core.dataClasses.Folder;
import project.core.dataClasses.SingleFile;
import project.core.dataClasses.User;
import project.core.exceptions.FileNotFoundException;
import project.core.exceptions.FolderNotFoundException;
import project.core.exceptions.NoFreeSpaceException;

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

        long freeSpace = getFreeScape(singleFile.getOwner());
        if (freeSpace < singleFile.getSize()) {
            throw new NoFreeSpaceException();
        }

        Integer id = idCounterFile.incrementAndGet();
        singleFile.setId(id);
        files.putIfAbsent(id, singleFile);
        return id;
    }

    @Override
    public Integer addFile(SingleFile singleFile, int folderId) {
        long freeSpace = getFreeScape(singleFile.getOwner());
        if (freeSpace < singleFile.getSize()) {
            throw new NoFreeSpaceException();
        }
        if (!checkIfFolderExists(folderId, singleFile.getOwner())) {
            throw new FolderNotFoundException(folderId);
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
    public Collection<SingleFile> getAllFiles(String user) {
        Set<SingleFile> filesToReturn = new HashSet<SingleFile>();
        for (SingleFile file : files.values()) {
            String owner = file.getOwner();
            if (owner.equals(user) || (file.getSharedUsers() != null && file.getSharedUsers().contains(user))) {
                filesToReturn.add(file);
            }
        }
        return filesToReturn;
    }

    @Override
    public Collection<SingleFile> getAllFilesFromFolder(int folderId, String user) {
        if (!checkIfFolderExists(folderId, user)) {
            return null;
        }
        if (files.isEmpty()) {
            return Collections.emptySet();
        }
        Set<SingleFile> filesFromFolder = new HashSet<>();
        for (SingleFile file : files.values()) {
            String owner = file.getOwner();
            if (file.getFolderId() != null) {
                if (file.getFolderId() == folderId && owner.equals(user) || (file.getFolderId() == folderId && file.getSharedUsers() != null && file.getSharedUsers().contains(user))) {
                    filesFromFolder.add(file);
                }
            }
        }
        return filesFromFolder;
    }

    @Override
    public Collection<Folder> getAllFolders(String user) {
        Set<Folder> foldersToReturn = new HashSet<Folder>();

        for (Folder folder : folders.values()) {
            String owner = folder.getOwner();
            if (owner.equals(user)) {
                foldersToReturn.add(folder);
            }
        }

        return foldersToReturn;
    }

    @Override
    public SingleFile getOneFile(Integer id, String user) {
        if (files.containsKey(id)) {
            SingleFile file = files.get(id);
            String owner = file.getOwner();
            if (owner.equals(user) || (file.getSharedUsers() != null && file.getSharedUsers().contains(user)))
                return file;
        }
        return null;

    }

    @Override
    public Folder getOneFolder(Integer id, String user) {
        if (folders.containsKey(id)) {
            String owner = folders.get(id).getOwner();
            if (owner.equals(user))
                return folders.get(id);
        }
        return null;
    }

    @Override
    public void deleteFolder(Integer folderId, String user) {
        String owner = folders.get(folderId).getOwner();
        if (owner.equals(user)) {
            folders.remove(folderId);
        } else {
            throw new FolderNotFoundException(folderId);
        }

        for (SingleFile file : files.values()) {
            if (file.getFolderId() == folderId && file.getOwner() == user) {
                files.remove(file.getId());
            }
        }
    }

    @Override
    public void deleteFile(Integer file, String user) {
        String owner = files.get(file).getOwner();
        if (owner.equals(user)) {
            files.remove(file);
            return;
        } else {
            throw new IllegalArgumentException("User has no file with this id");
        }
    }

    @Override
    public void updateFolder(Folder folder, String user) {
        if (null == folder.getId()) {
            throw new IllegalArgumentException("Folder has no id");
        } else if (folder.getOwner() == user) {
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

    @Override
    public Boolean checkIfFolderExists(int folderId, String user) {
        if (!folders.containsKey(folderId)) {
            return false;
        } else {
            String owner = folders.get(folderId).getOwner();
            if (!owner.equals(user)) {
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public void shareFile(Integer fileId, String owner, Set<User> users) {
        if (!files.containsKey(fileId)) {
            throw new FileNotFoundException(fileId);
        } else {
            SingleFile file = files.get(fileId);
            String fileOwner = file.getOwner();
            if (fileOwner.equals(owner)) {
                for (User user : users) {
                    file.addSharedUser(user.getUsername());
                }
            } else {
                throw new FileNotFoundException(fileId);
            }
        }
    }

    @Override
    public void moveFile(SingleFile file, String user) {
        if (!files.containsKey(file.getId()) || !files.get(file.getId()).getOwner().equals(user)) {
            throw new FileNotFoundException(file.getId());
        } else {
            SingleFile fileToUpdate = files.get(file.getId());
            fileToUpdate.setFolderId(file.getFolderId());

        }
    }

}
