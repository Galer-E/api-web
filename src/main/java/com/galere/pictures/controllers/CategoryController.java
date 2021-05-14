package com.galere.pictures.controllers;

import java.util.ArrayList;
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

/**
 * <b>
 * 	Controller offrant différentes routes pour gérer les catégories d'image en base de données.
 * </b>
 * 
 * @see Category
 * @see ICategoryService
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
@Controller
public class CategoryController {
	
	/**
	 * <b> Implémentation du service ICategoryService. </b>
	 */
    @Autowired
    private ICategoryService categoryService;
	
    /**
     * <b> Lister ou rechercher des catégories (par mots clés). </b>
     * 
     * @param tags Mots clés
     * @param model Attributs destinés à la page web.
     * @return La liste récupérée, vers la page de listing admin.
     */
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
	
	/**
     * <b> Redirection vers la page de création d'une catégorie. </b>
     * 
     * @param model Attributs destinés à la page web.
     * @return Une nouvelle catégorie vierge.
     */
	@RequestMapping(value = { "/admin/category/new" }, method = RequestMethod.GET)
    public String createCategory(Model model) {
		
		model.addAttribute("category", new Category());
		model.addAttribute("creation", "");
		
        return "admin/category/CategoryEdition";
        
    }
	
	/**
     * <b> Redirection vers la page d'édition d'une catégorie. </b>
     * 
     * @param id Id de la catégorie recherchée.
     * @param model Attributs destinés à la page web.
     * @return La catégorie existante.
     */
	@RequestMapping(value = { "/admin/categories/{id}/edit" }, method = RequestMethod.GET)
    public String editCategory(@PathVariable Long id, Model model) {
		
		if (categoryService.getRepository().existsById(id)) {
		
			model.addAttribute("category", categoryService.getRepository().findById(id).get());
			model.addAttribute("update", "");
			
	        return "admin/category/CategoryEdition";
        
		}
		
		return "/index";
        
    }
	
	/**
     * <b> Sauvegarder une catégorie. </b>
     * 
     * @param category Une catégorie.
     * @param model Attributs destinés à la page web.
     * @return La liste de toutes les catégories.
     * 
     * @see Category
     */
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
	
	/**
     * <b> Mise à jour d'une catégorie. </b>
     * 
     * @param category Une catégorie.
     * @param id L'id de la catégorie à mettre à jour.
     * @param model Attributs destinés à la page web.
     * @return La liste de toutes les catégories.
     * 
     * @see Category
     */
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
