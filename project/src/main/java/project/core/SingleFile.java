package project.core;

import java.io.File;

/**
 * Created by Marta on 2015-01-01.
 */
public class SingleFile {
    String name;
    long size;
    byte[] fileBytes;
    public SingleFile() {
    }

    public SingleFile(String name,  byte[] byteArray) {

        this.name = name;
        this.size = byteArray.length;
        this.fileBytes = byteArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }
}
