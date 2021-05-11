package com.galere.pictures.repositories;

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

/**
 * <b>
 * 	Class représentant le repository chargé de stoquer et retrouver les images sur la machine.
 * </b>
 * 
 * @see FileSystemResource
 * @see File
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
@Repository
public class FileSystemRepository {
	
	/**
	 * <b> Chemin d'accès aux dossier qui contient les images sur la machine. </b>
	 */
	String RESOURCES_DIR = FileSystemRepository.class.getResource("/").getPath();

	/**
	 * <b> Permet de sauvegarder une image sur la machine à partir de son nom et du tableau de byte qui la représente. </b>
	 * 
	 * @param content Tableau de byte représentant l'image.
	 * @param imageName Nom de l'image.
	 * @return Le chemin d'accès à l'image sauevgardée sur la machine.
	 * @throws Exception En cas d'erreur.
	 */
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
    
    /**
     * <b> Permet de retrouver et récupérer une image stoquée sur la machine à partir de son chemin d'accès. </b>
     * 
     * @param location Chemin d'accès vers l'image sur la machine.
     * @return L'image stoquée sur la machine.
     */
    public FileSystemResource findInFileSystem(String location) {
        try {
            return new FileSystemResource(new File(location));
        } catch (Exception e) {return null;}
    }
	
}
