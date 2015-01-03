package project.core;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by Marta on 2015-01-01.
 */
public class Folder {
    String name;
    Integer id;
    public Folder() {
    }
    public Folder(String name) {
        this.name = name;
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
}
