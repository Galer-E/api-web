package com.galere.pictures.controllers;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.galere.pictures.config.SpringSecurityConfig;
import com.galere.pictures.entities.Role;
import com.galere.pictures.entities.User;
import com.galere.pictures.services.IEncryptionService;
import com.galere.pictures.services.IRoleService;
import com.galere.pictures.services.IUserService;

/**
 * <b>
 * 	Controller offrant différentes routes pour gérer les utilisateurs, en base de données et dans la mémoire
 * 	de l'application.
 * </b>
 * 
 * @see User
 * @see IUserService
 * @see Role
 * @see IRoleService
 * @see IEncryptionService
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
@Controller
public class UserController {
	
	/**
	 * <b> Implémentation du service IUserService. </b>
	 */
    @Autowired
    private IUserService userService;
    
    /**
	 * <b> Implémentation du service IRoleService. </b>
	 */
    @Autowired
    private IRoleService roleService;
    
    /**
	 * <b> Implémentation du service IEncryptionService. </b>
	 */
    @Autowired
    private IEncryptionService encryption;
    
    /**
	 * <b> Instance de InMemoryUserDetailsManager envoyée depuis la class SpringSecurityConfig. </b>
	 */
    private final InMemoryUserDetailsManager memory;
    
    /**
     * <b> Constructeur du controller, nécessaire pour obtenir l'instance de inMemoryUserDetailsManager. </b>
     * 
     * @param inMemoryUserDetailsManager Accès à la gestion des utilisateurs en mémoire.
     */
    @Autowired
    public UserController(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
       this.memory = inMemoryUserDetailsManager;
    }
    
    /**
     * <b> Lister ou rechercher des utilisateurs (par mots clés). </b>
     * 
     * @param tags Mots clés
     * @param model Attributs destinés à la page web.
     * @return La liste récupérée, vers la page de listing admin.
     */
    @RequestMapping(value = { "/admin/users" }, method = RequestMethod.GET)
    public String listUsers(@RequestParam(value = "tags", required = false) String tags, 
    						Model model) {
		
		List<User> users;
		
		if (tags != null && !tags.equals(""))
			users = userService.searchByTags(tags);
		else
			users = userService.getRepository().findAll();
		for (User u : users)
			u.hidePass();
		
		model.addAttribute("users", users);
        return "admin/user/UserList";
        
    }
	
    /**
     * <b> Redirection vers la page de création d'un utilisateur. </b>
     * 
     * @param model Attributs destinés à la page web.
     * @return Un nouvel utilisateur vierge.
     */	@RequestMapping(value = { "/admin/user/new" }, method = RequestMethod.GET)
    public String createUser(Model model) {
		
		model.addAttribute("user", new User());
		model.addAttribute("creation", "");
		model.addAttribute("roles", roleService.getRepository().findAll());
        return "admin/user/UserEdition";
        
    }
	
     /**
      * <b> Redirection vers la page d'édition d'un utilisateur. </b>
      * 
      * @param id Id de de l'utilisateur recherché.
      * @param model Attributs destinés à la page web.
      * @return L'utilisateur existant.
      */
	@RequestMapping(value = { "/admin/users/{id}/edit" }, method = RequestMethod.GET)
    public String editUser(@PathVariable Long id, Model model) {
		
		if (userService.getRepository().existsById(id)) {
		
			model.addAttribute("user", userService.getRepository().findById(id).get());
			model.addAttribute("update", "");
			model.addAttribute("roles", roleService.getRepository().findAll());
	        return "admin/user/UserEdition";
        
		}
		
		return "/index";
        
    }
	
	/**
	 * <b> Redirection vers la page de suppression d'un utilisateur. </b>
	 * 
	 * <p> Cette page sert de validation à la suppression. </p>
	 * 
	 * @param id Id de l'utilisateur.
	 * @param model Attributs destinés à la page web.
	 * @return La page de validation de la suppression.
	 */
	@RequestMapping(value = { "/admin/users/{id}/delete" }, method = RequestMethod.GET)
    public String removeUser(@PathVariable Long id, Model model) {
		
		if (userService.getRepository().existsById(id)) {
		
			model.addAttribute("user", userService.getRepository().findById(id).get());
			model.addAttribute("roles", roleService.getRepository().findAll());
	        return "admin/user/UserDeletion";
        
		}
		
		return "/index";
        
    }
	
	/**
     * <b> Sauvegarder un utilisateur. </b>
     * 
     * @param user Un utilisateur.
     * @param model Attributs destinés à la page web.
     * @return La liste de tous les utilisateurs.
     * 
     * @see Role
     */	@RequestMapping(value = { "/admin/users" }, method = RequestMethod.POST)
    public String saveUser(@Valid @ModelAttribute("user") User user, Model model) {
		
		if (StringUtils.isEmpty(user.getLogin()) || StringUtils.isEmpty(user.getPass())) {
		
			model.addAttribute("user", user);
			model.addAttribute("creation", "");
			model.addAttribute("roles", roleService.getRepository().findAll());
			model.addAttribute("creationError", "");
	
	        return "admin/user/UserEdition";
        
		} else if (userService.existsUser(user.getLogin())) {
	        
			model.addAttribute("user", user);
			model.addAttribute("creation", "");
			model.addAttribute("roles", roleService.getRepository().findAll());
			model.addAttribute("existsError", "");
	
	        return "admin/user/UserEdition";
			
		} else {
			
			try {
				
				user.setEntry(LocalDate.now());
				user.setPass(encryption.encrypt(user.getPass()));
				userService.getRepository().save(user);
				
				UserDetails details = org.springframework.security.core.userdetails.User
										.withUsername(user.getLogin())
										.password("{noop}" + encryption.decrypt(user.getPass()))
										.roles(user.getHeightRole().getLevel() > 0 ? "ADMIN" : "USER").build();
				memory.createUser(details);
			
			} catch (Exception e) {e.printStackTrace();}

			
			model.addAttribute("creationOK", "");
			model.addAttribute("users", userService.getRepository().findAll());
			
			return "/admin/user/UserList";
			
		}
        
    }
	
     /**
      * <b> Mise à jour d'un utilisateur. </b>
      * 
      * @param user Un utilisateur.
      * @param id L'id de de l'utilisateur à mettre à jour.
      * @param model Attributs destinés à la page web.
      * @return La liste de tous les utilisateurs.
      * 
      * @see User
      */
     @RequestMapping(value = { "/admin/users/{id}" }, method = RequestMethod.POST)
    public String updateUser(@Valid @ModelAttribute("user") User user, @PathVariable Long id, Model model) {
			
		User old = userService.getRepository().findById(id).get();
		
		if (StringUtils.isEmpty(user.getLogin()) || StringUtils.isEmpty(user.getPass())) {
		
			model.addAttribute("user", old);
			model.addAttribute("update", "");
			model.addAttribute("roles", roleService.getRepository().findAll());
			model.addAttribute("creationError", "");
	
	        return "admin/user/UserEdition";
        
		} else {
			
			try {
				
				user.setEntry(old.getEntry());
				user.setPass(encryption.encrypt(user.getPass()));
				userService.getRepository().save(user);
				
				UserDetails details = org.springframework.security.core.userdetails.User
										.withUsername(user.getLogin())
										.password("{noop}" + encryption.decrypt(user.getPass()))
										.roles(user.getHeightRole().getLevel() > 0 ? "ADMIN" : "USER").build();
				memory.deleteUser(old.getLogin());
				memory.createUser(details);
			
			} catch (Exception e) {e.printStackTrace();}

			
			model.addAttribute("updateOK", "");
			model.addAttribute("users", userService.getRepository().findAll());
			
			return "/admin/user/UserList";
			
		}
        
    }
	
     /**
 	 * <b> Suppression d'un utilisateur en base de données et en mémoire. </b>
 	 * 
 	 * @param id Id de l'utilisateur.
 	 * @param model Attributs destinés à la page web.
 	 * @return La suppression, puis redirection vers la page de listing admin.
 	 */	@RequestMapping(value = { "/admin/users/{id}/delete" }, method = RequestMethod.POST)
    public String deleteUser(@PathVariable Long id, Model model) {
			
		User old = userService.getRepository().findById(id).get();
		
		if (userService.getRepository().existsById(id)) {
			
			try {
				
				userService.getRepository().deleteById(old.getId());
				memory.deleteUser(old.getLogin());
			
			} catch (Exception e) {e.printStackTrace();}

			
			model.addAttribute("deleteOK", "");
			model.addAttribute("users", userService.getRepository().findAll());
			
			return "/admin/user/UserList";
			
		}
		
		return "/index";
        
    }
	
}
