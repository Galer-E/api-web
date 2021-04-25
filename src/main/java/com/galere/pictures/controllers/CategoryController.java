package com.galere.pictures.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.galere.pictures.entities.Category;
import com.galere.pictures.services.ICategoryService;

@Controller
public class CategoryController {
	
    @Autowired
    private ICategoryService categoryService;
	

    //	Lister ou rechercher des catégories (par mots clés)
	@RequestMapping(value = { "/admin/categories" }, method = RequestMethod.GET)
    public String listCategories(@RequestParam(value = "tags", required = false) String tags, 
    						Model model) {
		
		List<Category> categories;
		
		if (tags != null && !tags.equals(""))
			categories = categoryService.searchByTags(tags);
		else
			categories = categoryService.getRepository().findAll();
		
		model.addAttribute("categories", categories);
        return "admin/category/CategoryList";
        
    }
	
	//	Redirect to categories creation page
	@RequestMapping(value = { "/admin/category/new" }, method = RequestMethod.GET)
    public String createCategory(Model model) {
		
		model.addAttribute("category", new Category());
		model.addAttribute("creation", "");
		
        return "admin/category/CategoryEdition";
        
    }
	
	//	Redirect to category edit page
	@RequestMapping(value = { "/admin/categories/{id}/edit" }, method = RequestMethod.GET)
    public String editCategory(@PathVariable Long id, Model model) {
		
		if (categoryService.getRepository().existsById(id)) {
		
			model.addAttribute("category", categoryService.getRepository().findById(id).get());
			model.addAttribute("update", "");
			
	        return "admin/category/CategoryEdition";
        
		}
		
		return "/index";
        
    }
	
	//	Save role into data base
	@RequestMapping(value = { "/admin/categories" }, method = RequestMethod.POST)
    public String saveCategory(@Valid @ModelAttribute("category") Category category, Model model) {
		
		if (StringUtils.isEmpty(category.getLabel())) {
		
			model.addAttribute("category", category);
			model.addAttribute("creation", "");
			model.addAttribute("creationError", "");
	
	        return "admin/category/CategoryEdition";
        
		} else if (categoryService.existsCategory(category.getLabel())) {
	        
			model.addAttribute("category", category);
			model.addAttribute("creation", "");
			model.addAttribute("existsError", "");
	
	        return "admin/category/CategoryEdition";
			
		} else {
			
			categoryService.getRepository().save(category);
			
			model.addAttribute("creationOK", "");
			model.addAttribute("categories", categoryService.getRepository().findAll());
			
			return "/admin/category/CategoryList";
			
		}
        
    }
	
	//	Update role from data base
	@RequestMapping(value = { "/admin/categories/{id}" }, method = RequestMethod.POST)
    public String updateCategory(@Valid @ModelAttribute("category") Category category, @PathVariable Long id, Model model) {
			
		Category old = categoryService.getRepository().findById(id).get();
		
		if (StringUtils.isEmpty(category.getLabel())) {
		
			model.addAttribute("category", old);
			model.addAttribute("update", "");
			model.addAttribute("creationError", "");
	
	        return "admin/category/CategoryEdition";
        
		} else {
			
			categoryService.getRepository().save(category);
			
			model.addAttribute("updateOK", "");
			model.addAttribute("categories", categoryService.getRepository().findAll());
			
			return "/admin/category/CategoryList";
			
		}
        
    }
	
}
