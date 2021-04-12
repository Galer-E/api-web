package com.galere.pictures.controllers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
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

import com.galere.pictures.entities.User;
import com.galere.pictures.services.IEncryptionService;
import com.galere.pictures.services.IRoleService;
import com.galere.pictures.services.IUserService;

@Controller
public class UserController {
	
    @Autowired
    private IUserService userService;
    
    @Autowired
    private IRoleService roleService;
    
    @Autowired
    private IEncryptionService encryption;
    
    private final InMemoryUserDetailsManager memory;
    
    @Autowired
    public UserController(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
       this.memory = inMemoryUserDetailsManager;
    }
    
    //	Lister ou rechercher des utilisateurs (par mots clés)
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
	
	//	Redirect to user creation page
	@RequestMapping(value = { "/admin/user/new" }, method = RequestMethod.GET)
    public String createUser(Model model) {
		
		model.addAttribute("user", new User());
		model.addAttribute("creation", "");
		model.addAttribute("roles", roleService.getRepository().findAll());
        return "admin/user/UserEdition";
        
    }
	
	//	Redirect to user edit page
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
	
	//	Redirect to user deletion page
	@RequestMapping(value = { "/admin/users/{id}/delete" }, method = RequestMethod.GET)
    public String removeUser(@PathVariable Long id, Model model) {
		
		if (userService.getRepository().existsById(id)) {
		
			model.addAttribute("user", userService.getRepository().findById(id).get());
			model.addAttribute("roles", roleService.getRepository().findAll());
	        return "admin/user/UserDeletion";
        
		}
		
		return "/index";
        
    }
	
	//	Save user into data base
	@RequestMapping(value = { "/admin/users" }, method = RequestMethod.POST)
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
	
	//	Update user from data base
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
	
	//	Delete user from data base
	@RequestMapping(value = { "/admin/users/{id}/delete" }, method = RequestMethod.POST)
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
