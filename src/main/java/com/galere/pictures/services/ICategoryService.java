package com.galere.pictures.services;

import java.util.List;

import com.galere.pictures.entities.Category;
import com.galere.pictures.repositories.CategoryRepository;

public interface ICategoryService {
	
	public CategoryRepository getRepository();
	
	public boolean existsCategory(String label);
	public List<Category> searchByTags(String tags);
	
}
