package com.galere.pictures.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.galere.pictures.entities.Role;
import com.galere.pictures.services.IRoleService;

@Controller
public class RoleController {
	
    @Autowired
    private IRoleService roleService;
	
	@RequestMapping(value = { "/roles" }, method = RequestMethod.GET)
    public String listClient(Model model) {
		
		List<Role> roles = roleService.getRepository().findAll();
		
		model.addAttribute("roles", roles);
        return "admin/role/RoleList";
        
    }
	
}
