package com.galere.pictures.services.impl;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import com.galere.pictures.entities.Image;
import com.galere.pictures.repositories.FileSystemRepository;
import com.galere.pictures.repositories.ImageRepository;
import com.galere.pictures.services.IImageService;

@Service
public class ImageServiceImpl implements IImageService {

	@Autowired
    private FileSystemRepository fileRepository;
	
	@Autowired
	private ImageRepository repository;
	
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
	
}
