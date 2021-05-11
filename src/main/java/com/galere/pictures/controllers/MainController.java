package com.galere.pictures.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.galere.pictures.config.ApplicationEndHandle;
import com.galere.pictures.entities.Image;
import com.galere.pictures.entities.User;
import com.galere.pictures.services.IEncryptionService;
import com.galere.pictures.services.IImageService;
import com.galere.pictures.services.IUserService;

/**
 * <b>
 * 	Controller offrant différentes routes destinés aux actions usuelles : connexion, gestion espace utilisateur,
 * 	redirection vers les différentes pages usuelles.
 * </b>
 * 
 * @see User
 * @see IUserService
 * @see Image
 * @see IImageService
 * @see IEncryptionService
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
@Controller
public class MainController {
	
	private final static Logger log = LoggerFactory.getLogger(MainController.class);
	
	/**
	 * <b> Implémentation du service IUserService. </b>
	 */
    @Autowired
    private IUserService users;
    
    /**
	 * <b> Implémentation du service IEncryptionService. </b>
	 */
    @Autowired
    private IEncryptionService encryption;
    
    /**
	 * <b> Implémentation du service IImageService. </b>
	 */
    @Autowired
    private IImageService images;
	
    /**
     * <b> Méthode à appeler avant d'éteindre l'application si elle n'est pas stoppée correctement. </b>
     * 
     * @param model Attributs destinés à la page web.
     * @return La page d'index.
     */
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
	
	/**
	 * <b> Accéder à son espace personnel (utilisateur), nécessite d'être connecté. </b>
	 * 
	 * @param model Attributs destinés à la page web.
	 * @return La page d'espace utilisateur.
	 */
	@RequestMapping(value = "/user/me", method = RequestMethod.GET)
    public String me(Model model) {
        return "user/Space";
    }
	
	/**
	 * <b> Accéder à son espace personnel (utilisateur), nécessite d'être connecté. </b>
	 * 
	 * @param model Attributs destinés à la page web.
	 * @return La page d'espace utilisateur.
	 */
	@RequestMapping(value = "/shared", method = RequestMethod.GET)
    public String shared(@RequestParam(value = "tags", required = false) String tags,
    					Model model) {
		
		List<Image> imgs;
		
		if (tags != null && !tags.equals(""))
			imgs = images.searchByTags(tags);
		else
			imgs = images.getRepository().findAll();
		
		model.addAttribute("images", imgs);
        return "shared/index";
    }
    
    /**
     * <b> Page d'index, ou de connexion si l'utilisateur n'est pas encore connecté. </b>
     * 
     * <p> La détection de la page à afficher est faite sur le template. </p>
     * 
     * @param model Attributs destinés à la page web.
     * @return La page d'index ou de connexion.
     */
	@RequestMapping(value = { "/", "index", "login" }, method = RequestMethod.GET)
    public String home(Model model) {
		
		model.addAttribute("images", images.getRepository().findAll());
		
        return "index";
    }
	
    /**
     * <b> Redirection vers la page de connexion spécifiquement. </b>
     * 
     * @param model Attributs destinés à la page web.
     * @return Redirection vers la page de connexion.
     */
	@RequestMapping(value = "login", method = RequestMethod.POST)
    public String homeConnection(Model model) {
        return "/index";
    }
	
	/**
	 * <b> Route appelée lorsqu'une erreur est détéctée à la connexion d'un utilisateur. </b>
	 * 
	 * @param model Attributs destinés à la page web.
	 * @return La page de connexion, avec l'attribut correspondant à l'erreur.
	 */
	@RequestMapping(value = "login-error")
    public String homeConnectionError(Model model) {
		model.addAttribute("loginError", true);
	    return "index";
    }
	
}
