package com.galere.pictures.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.galere.pictures.services.IEncryptionService;
import com.galere.pictures.services.IUserService;

@Controller
public class MainController {
	
    @Autowired
    private IUserService users;
    
    @Autowired
    private IEncryptionService encryption;
	
    // Test
	@RequestMapping(value = "test", method = RequestMethod.GET)
    public String test(Model model) {
		
		encryption.reloadKey();
		
        return "index";
    }
    
    // Home / Login page
	@RequestMapping(value = { "/", "index", "login" }, method = RequestMethod.GET)
    public String home(Model model) {
        return "index";
    }
	
    // Home / Login page
	@RequestMapping(value = "login", method = RequestMethod.POST)
    public String homeConnection(Model model) {
        return "index";
    }
	
	// Home / Login page
	@RequestMapping(value = "login-error")
    public String homeConnectionError(Model model) {
		model.addAttribute("loginError", true);
	    return "index";
    }
	
}
