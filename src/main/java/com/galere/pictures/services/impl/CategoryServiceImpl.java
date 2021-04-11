package com.galere.pictures.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.galere.pictures.repositories.CategoryRepository;
import com.galere.pictures.services.ICategoryService;

@Service
public class CategoryServiceImpl implements ICategoryService {

	@Autowired
	private CategoryRepository repository;
	
	@Override
	public CategoryRepository getRepository() {
		return repository;
	}
	
}
