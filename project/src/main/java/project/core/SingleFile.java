package project.core;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.Set;

/**
 * Created by Marta on 2015-01-01.
 */
public class SingleFile {
    String name;
    long size;
    byte[] fileBytesArray;
    int folderId;
    Integer id;
    String owner;
    Set<String> sharedUsers;

    public SingleFile() {
    }

    public SingleFile(String name, byte[] fileBytesArray, String owner) {

        this.name = name;
        this.size = fileBytesArray.length;
        this.fileBytesArray = fileBytesArray;
        this.owner = owner;
    }

    public SingleFile(String name, byte[] fileBytesArray, int folderId, String owner) {
        this.name = name;
        this.fileBytesArray = fileBytesArray;
        this.folderId = folderId;
        this.size = fileBytesArray.length;
        this.owner = owner;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public byte[] getFileBytesArray() {
        return fileBytesArray;
    }

    public void setFileBytesArray(byte[] fileBytesArray) {
        this.fileBytesArray = fileBytesArray;
    }

    @XmlAttribute
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlElement
    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Set<String> getSharedUsers() {
        return sharedUsers;
    }

    public void setSharedUsers(Set<String> sharedUsers) {
        this.sharedUsers = sharedUsers;
    }
}
