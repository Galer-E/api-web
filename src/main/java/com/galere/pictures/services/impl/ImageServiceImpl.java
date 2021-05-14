package com.galere.pictures.services.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import com.galere.pictures.entities.Image;
import com.galere.pictures.repositories.FileSystemRepository;
import com.galere.pictures.repositories.ImageRepository;
import com.galere.pictures.services.IImageService;

/**
 * <b>
 * 	Implémentation du service IImageService.
 * </b>
 * 
 * @see IImageService
 * @see Image
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
@Service
public class ImageServiceImpl implements IImageService {
	
	private static final int[][] formats = { { 100, 100 }, 
											{ 200, 200 }, 
											{ 300, 300 }, 
											{ 400, 400 }, 
											{ 500, 500 }, 
											{ 600, 600 }, 
											{ 700, 700 }, 
											{ 800, 800 }, 
											{ 900, 900 }, 
											{ 1000, 1000} };
	private static final int formatsWidth = 2, formatsHeight = 10;
	
	/**
	 * <b> Instance du repository FileSystemRepository. </b>
	 * 
	 * @see FileSystemRepository
	 */
	@Autowired
    private FileSystemRepository fileRepository;
	
	/**
	 * <b> Instance du repository ImageRepository. </b>
	 * 
	 * @see ImageRepository
	 */
	@Autowired
	private ImageRepository repository;
	
	@Override
	public FileSystemRepository getFileRepository() {
		return fileRepository;
	}
	
	@Override
	public ImageRepository getRepository() {
		return repository;
	}

	DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(
	        DateFormat.MEDIUM,
	        DateFormat.MEDIUM);
	
	@Override
	public Image saveImage(Image img, byte[] content, String name) {
		try {
			String uniq = "";
			for (String part : new Date().toString().split(" "))
				uniq += part;
			uniq += "-" + name;
			
			uniq = verify(uniq);
			
			img.setUrl(fileRepository.save(content, uniq));
			return getRepository().save(img);
		} catch (Exception e) {e.printStackTrace();}
		
		return null;
	}

	/**
	 * <b> 
	 * 	Vérifier que la la chaine de carractères passée en paramètre ne contient aucun carractère interdit
	 * 	dans un nom de fichier. Si la chaine contient des carractères interdits, ils seront remplacés par des
	 * 	tirets.
	 * </b>
	 * 
	 * @param uniq La chaine de carractères à vérifier.
	 * @return La chaine de carractères avec les carractères interdits remplacés par des tirets.
	 */
	private String verify(String uniq) {
		if (uniq.contains("\\"))
			uniq = uniq.replace("\\", "-");
		if (uniq.contains("/"))
			uniq = uniq.replace("/", "-");
		if (uniq.contains(":"))
			uniq = uniq.replace(":", "-");
		if (uniq.contains("*"))
			uniq = uniq.replace("*", "-");
		if (uniq.contains("?"))
			uniq = uniq.replace("?", "-");
		if (uniq.contains("\""))
			uniq = uniq.replace("\"", "-");
		if (uniq.contains("<"))
			uniq = uniq.replace("<", "-");
		if (uniq.contains(">"))
			uniq = uniq.replace(">", "-");
		if (uniq.contains("|"))
			uniq = uniq.replace("|", "-");
		return uniq;
	}

	@Override
	public FileSystemResource findImage(Long id) {
		if (getRepository().existsById(id))
			return fileRepository.findInFileSystem(getRepository().findById(id).get().getUrl());
		return null;
	}
	
	@Override
	public boolean existsImage(String title) {
		return getRepository().findAll().stream().filter(role -> role.getTitle().equalsIgnoreCase(title)).count() > 0;
	}
	
	@Override
	public List<Image> searchByTags(String tags) {
		List<String> tagList = Arrays.asList(tags.split(" "));
		
		return getRepository().findAll().stream().filter(
					i -> tagList.stream().filter(
									tag -> i.getTitle().contains(tag) ||
										   i.getId().toString().contains(tag) ||
										   i.getDescription().contains(tag) ||
										   i.getTags().contains(tag) ||
										   i.getContent().contains(tag) ||
										   i.getDate().toString().equalsIgnoreCase(tag) ||
										   i.getStringCategories().contains(tag)
										   
						).count() > 0
				).collect(Collectors.toList());
	}
	
	@Override
	public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
	    BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics2D = resizedImage.createGraphics();
	    graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
	    graphics2D.dispose();
	    return resizedImage;
	}

	@Override
	public List<File> getGroupSizeOf(Long id) throws Exception {
		
		ArrayList<File> list = new ArrayList<File>();
		FileSystemResource initial = findImage(id);
		
		for (int i = 0; i < formatsHeight; i++) {
			list.add(
					getResizedImage(initial, formats[i][0], formats[i][1]).getFile()
				);
		}
		
		return list;
	}

	@Override
	public FileSystemResource getResizedImage(FileSystemResource initial, Integer width, Integer height) throws Exception {

		File f = new File(initial.getFile().getPath() + "-" + width + "x" + height + ".png");
		FileSystemResource img;
		
		if (!f.exists()) {
			BufferedImage resized = resizeImage(ImageIO.read(initial.getFile()), width, height);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(resized, "png", baos);
	        byte[] resizedBytes = baos.toByteArray();
	        
	        img = new FileSystemResource(
	        		getFileRepository().save(resizedBytes, initial.getFilename() + "-" + width + "x" + height + ".png"));
		} else
			img = getFileRepository().findInFileSystem(f.getPath());
		
		return img;
	}
	
}
