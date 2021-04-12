package com.galere.pictures.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.galere.pictures.config.ApplicationEndHandle;
import com.galere.pictures.services.IEncryptionService;
import com.galere.pictures.services.IUserService;

@Controller
public class MainController {
	
	private final static Logger log = LoggerFactory.getLogger(MainController.class);
	
    @Autowired
    private IUserService users;
    
    @Autowired
    private IEncryptionService encryption;
	
    // Test
	@RequestMapping(value = "/admin/prepare-end", method = RequestMethod.GET)
    public String prepareEnd(Model model) {
		
		log.info("----- END OF APPLICATION DETECTED -----");
        ApplicationEndHandle.reloader.end();
        log.info("Saving users..."); 
        encryption.loadInitialKey();
        log.info("OK");
        log.info("----- END OF APPLICATION DETECTED -----");
		
        return "index";
    }
	
	// User Space
	@RequestMapping(value = "/user/me", method = RequestMethod.GET)
    public String me(Model model) {
        return "user/Space";
    }
    
    // Home / Login page
	@RequestMapping(value = { "/", "index", "login" }, method = RequestMethod.GET)
    public String home(Model model) {
        return "index";
    }
	
    // Home / Login page
	@RequestMapping(value = "login", method = RequestMethod.POST)
    public String homeConnection(Model model) {
        return "/index";
    }
	
	// Home / Login error page
	@RequestMapping(value = "login-error")
    public String homeConnectionError(Model model) {
		model.addAttribute("loginError", true);
	    return "index";
    }
	
}
