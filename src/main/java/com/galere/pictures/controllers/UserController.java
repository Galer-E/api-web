package com.galere.pictures.controllers;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.galere.pictures.entities.User;
import com.galere.pictures.services.IEncryptionService;
import com.galere.pictures.services.IUserService;

@Controller
public class UserController {
	
    @Autowired
    private IUserService userService;
    
    @Autowired
    private IEncryptionService encryption;
	
	@RequestMapping(value = { "/admin/users" }, method = RequestMethod.GET)
    public String listClient(Model model) {
		
		List<User> users = userService.getRepository().findAll();
		for (User u : users)
			u.hidePass();
		
		model.addAttribute("users", userService.getRepository().findAll());
        return "admin/user/UserList";
        
    }
	
}
