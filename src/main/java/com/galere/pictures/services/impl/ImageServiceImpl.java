package com.galere.pictures.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.galere.pictures.repositories.ImageRepository;
import com.galere.pictures.services.IImageService;

@Service
public class ImageServiceImpl implements IImageService {

	@Autowired
	private ImageRepository repository;
	
	@Override
	public ImageRepository getRepository() {
		return repository;
	}
	
}
