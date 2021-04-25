package com.galere.pictures.repositories;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

@Repository
public class FileSystemRepository {
	
	String RESOURCES_DIR = FileSystemRepository.class.getResource("/").getPath();

    public String save(byte[] content, String imageName) throws Exception {
    	        
        File f = new File(RESOURCES_DIR);
        
        if (!f.exists())
        	f.mkdirs();
        
        f = new File(RESOURCES_DIR + imageName);
        System.out.println(f.getPath());
        f.createNewFile();
        
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(content);
        fo.flush();
        fo.close();

        return f.getAbsolutePath().toString();
        
    }
    
    public FileSystemResource findInFileSystem(String location) {
        try {
            return new FileSystemResource(new File(location));
        } catch (Exception e) {throw new RuntimeException();}
    }
	
}
