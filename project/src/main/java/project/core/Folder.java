package project.core;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.Set;

/**
 * Created by Marta on 2015-01-01.
 */
public class Folder {
    String name;
    Integer id;
    String owner;
    Set<String> sharedUsers;

    public Folder() {
    }
    public Folder(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }
    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
