package project.core;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Marta on 2015-01-02.
 */
public enum FileSingleton {
    INSTANCE;


    private Map<Integer, SingleFile> files = new ConcurrentHashMap<>();
    public SingleFile getOne(Integer id){
        return files.get(id);
    }
    public int create(SingleFile file){

        int newId= files.size();
file.setId(newId);
        files.put(newId, file);
        return newId;
    }
}
