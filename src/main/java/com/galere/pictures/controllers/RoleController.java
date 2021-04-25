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

import com.galere.pictures.entities.Role;
import com.galere.pictures.services.IRoleService;

@Controller
public class RoleController {
	
    @Autowired
    private IRoleService roleService;
	
    //	Lister ou rechercher des roles (par mots clés)
	@RequestMapping(value = { "/admin/roles" }, method = RequestMethod.GET)
    public String listRoles(@RequestParam(value = "tags", required = false) String tags, 
    						Model model) {
		
		List<Role> roles;
		
		if (tags != null && !tags.equals(""))
			roles = roleService.searchByTags(tags);
		else
			roles = roleService.getRepository().findAll();
		
		model.addAttribute("roles", roles);
        return "admin/role/RoleList";
        
    }
	
	//	Redirect to role creation page
	@RequestMapping(value = { "/admin/role/new" }, method = RequestMethod.GET)
    public String createRole(Model model) {
		
		model.addAttribute("role", new Role());
		model.addAttribute("creation", "");
		
        return "admin/role/RoleEdition";
        
    }
	
	//	Redirect to role edit page
	@RequestMapping(value = { "/admin/roles/{id}/edit" }, method = RequestMethod.GET)
    public String editUser(@PathVariable Long id, Model model) {
		
		if (roleService.getRepository().existsById(id)) {
		
			model.addAttribute("role", roleService.getRepository().findById(id).get());
			model.addAttribute("update", "");
			
	        return "admin/role/RoleEdition";
        
		}
		
		return "/index";
        
    }
	
	//	Save role into data base
	@RequestMapping(value = { "/admin/roles" }, method = RequestMethod.POST)
    public String saveRole(@Valid @ModelAttribute("role") Role role, Model model) {
		
		if (StringUtils.isEmpty(role.getLabel()) || role.getLevel() < 0) {
		
			model.addAttribute("role", role);
			model.addAttribute("creation", "");
			model.addAttribute("creationError", "");
	
	        return "admin/role/RoleEdition";
        
		} else if (roleService.existsRole(role.getLabel())) {
	        
			model.addAttribute("role", role);
			model.addAttribute("creation", "");
			model.addAttribute("existsError", "");
	
	        return "admin/role/RoleEdition";
			
		} else {
			
			roleService.getRepository().save(role);
			
			model.addAttribute("creationOK", "");
			model.addAttribute("roles", roleService.getRepository().findAll());
			
			return "/admin/role/RoleList";
			
		}
        
    }
	
	//	Update role from data base
	@RequestMapping(value = { "/admin/roles/{id}" }, method = RequestMethod.POST)
    public String updateRole(@Valid @ModelAttribute("role") Role role, @PathVariable Long id, Model model) {
			
		Role old = roleService.getRepository().findById(id).get();
		
		if (StringUtils.isEmpty(role.getLabel()) || role.getLevel() < 0) {
		
			model.addAttribute("role", old);
			model.addAttribute("update", "");
			model.addAttribute("creationError", "");
	
	        return "admin/role/RoleEdition";
        
		} else {
			
			roleService.getRepository().save(role);
			
			model.addAttribute("updateOK", "");
			model.addAttribute("roles", roleService.getRepository().findAll());
			
			return "/admin/role/RoleList";
			
		}
        
    }
	
}
