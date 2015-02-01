package project.core.dataClasses;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;

import javax.xml.bind.annotation.*;
import java.util.Set;

/**
 * Created by Marta on 2015-01-01.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Folder {
    String name;
    Integer id;
    String owner;
    Set<String> sharedUsers;

    public Folder() {
    }
    public Folder(String name) {
        this.name = name;

    }
    public Folder(String name, String owner) {
        this.name = name;
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
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return sharedUsers;
    }
    public void addSharedUser(String username){
        sharedUsers.add(username);
    }
    public void setSharedUsers(Set<String> sharedUsers) {
        this.sharedUsers = sharedUsers;
    }
}
