package com.galere.pictures.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.galere.pictures.entities.Category;
import com.galere.pictures.repositories.CategoryRepository;
import com.galere.pictures.services.ICategoryService;

/**
 * <b>
 * 	Implémentation du service ICategoryService.
 * </b>
 * 
 * @see ICategoryService
 * @see Category
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

	/**
	 * <b> Instance du repository CategoryRepository. </b>
	 * 
	 * @see CategoryRepository
	 */
	@Autowired
	private CategoryRepository repository;
	
	@Override
	public CategoryRepository getRepository() {
		return repository;
	}

	@Override
	public boolean existsCategory(String label) {
		return getRepository().findAll().stream().filter(cat -> cat.getLabel().equalsIgnoreCase(label)).count() > 0;
	}

	@Override
	public List<Category> searchByTags(String tags) {
		List<String> tagList = Arrays.asList(tags.split(" "));
		
		return getRepository().findAll().stream().filter(
					c -> tagList.stream().filter(
									tag -> c.getLabel().contains(tag) ||
										   c.getId().toString().contains(tag)
						).count() > 0
				).collect(Collectors.toList());
	}
	
}
