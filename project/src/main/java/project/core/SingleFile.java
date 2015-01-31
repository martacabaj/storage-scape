package project.core;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Marta on 2015-01-01.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
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
        sharedUsers = new HashSet<>();
        this.name = name;
        this.size = fileBytesArray.length;
        this.fileBytesArray = fileBytesArray;
        this.owner = owner;
    }

    public SingleFile(String name, byte[] fileBytesArray, int folderId, String owner) {
        sharedUsers = new HashSet<>();
        this.name = name;
        this.fileBytesArray = fileBytesArray;
        this.folderId = folderId;
        this.size = fileBytesArray.length;
        this.owner = owner;
    }

    public byte[] getFileBytesArray() {
        return fileBytesArray;
    }

    public void setFileBytesArray(byte[] fileBytesArray) {
        this.fileBytesArray = fileBytesArray;
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
    @XmlElement
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @XmlElementWrapper(name="shared-users")
    @XmlElement
    public Set<String> getSharedUsers() {

        sharedUsers.add("dfff");
        sharedUsers.add("dfff2");
        sharedUsers.add("dfff3");
        sharedUsers.add("dfff4");
        return sharedUsers;
    }

    public void setSharedUsers(Set<String> sharedUsers) {
        this.sharedUsers = sharedUsers;
    }
}
