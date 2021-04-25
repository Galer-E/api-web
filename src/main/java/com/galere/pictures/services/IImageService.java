package com.galere.pictures.services;

import java.util.List;

import org.springframework.core.io.FileSystemResource;

import com.galere.pictures.entities.Image;
import com.galere.pictures.repositories.ImageRepository;

public interface IImageService {
	
	public ImageRepository getRepository();
	
	public Image saveImage(Image img, byte[] content, String name);
	public FileSystemResource findImage(Long id);
	
	public boolean existsImage(String title);
	public List<Image> searchByTags(String tags);
	
}
