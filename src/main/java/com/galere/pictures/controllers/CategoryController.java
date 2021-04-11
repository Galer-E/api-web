package com.galere.pictures.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.galere.pictures.entities.Category;
import com.galere.pictures.services.ICategoryService;

@Controller
public class CategoryController {
	
    @Autowired
    private ICategoryService categoryService;
	
	@RequestMapping(value = { "/categories" }, method = RequestMethod.GET)
    public String listClient(Model model) {
		
		List<Category> categories = categoryService.getRepository().findAll();
		
		model.addAttribute("categories", categories);
        return "admin/category/CategoryList";
        
    }
	
}
