package com.galere.pictures.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.galere.pictures.config.core.ReloadKeyThread;
import com.galere.pictures.controllers.UserController;
import com.galere.pictures.entities.User;
import com.galere.pictures.services.IEncryptionService;
import com.galere.pictures.services.IUserService;

/**
 * <b>
 * 	Class de configuration. Permet de gérer différentes redirections, droits d'accès, les comptes utilisateurs
 * 	et les rôles.
 * </b>
 * <p> Les redirections gérés : </p>
 * 	<ul>
 * 		<li> '/login' pour le formulaire de connexion. </li>
 * 		<li> '/login-error' en cas d'erreur dans les informations de connexion. </li>
 * 		<li> '/index' en cas de déconnexion réussie. </li>
 * 		<li> '/403' en cas d'accès refusé. </li>
 * 	</ul>
 * <p> Les chemins nécessitants des droits d'accès, et les rôles autorisés : </p>
 * 	<ul>
 * 		<li> '/admin/**' nécessite que l'utilisateur connecté ait le rôle 'ADMIN'. </li>
 * 		<li> '/user/**' nécessite que l'utilisateur connecté ait le rôle 'USER' ou 'ADMIN'. </li>
 * 		<li> '/shared/**' nécessite que l'utilisateur connecté ait le rôle 'ADMIN' ou 'USER'. </li>
 * 	</ul>
 * <p>
 *	Au lancement de l'application, cette class sera utilisé et accèdera à la base de données pour récupérer les
 *	comptes utilisateurs stoqués ; les mots de passes seront déchiffrés avant l'enregistrement des comptes.
 * </p>
 * <p>
 * 	Si aucun compte n'est trouvé en base de données, alors un seul compte sera enregistré : admin/admin.
 * </p>
 * <p>
 * 	A la fin des opérations de cette class, le thread ReloadKeyThread de la class ApplicationEndHandle
 * 	sera instancié et lancé.
 * </p>
 * 
 * @see ReloadKeyThread
 * @see ApplicationEndHandle
 * @see IEncryptionService
 * @see IUserService
 * @see AuthenticationManagerBuilder
 * @see HttpSecurity
 * @see InMemoryUserDetailsManager
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	/**
	 * <b> Implémentation du service IUserService. Permet de gérer les utilisateurs stoqués en base de données. </b>
	 * 
	 * @see IUserService
	 */
	@Autowired
	private IUserService users;
	
	/**
	 * <b> Implémentation du service IEncryptionService. Permet de gérer le chiffrement et le déchiffrement. </b>
	 * 
	 * @see IEncryptionService
	 */
	@Autowired
	private IEncryptionService encryption;
	
	/**
	 * <b> Constructeur de cette class. </b>
	 */
    public SpringSecurityConfig() {
        super();
    }

    /**
     * <b> Méthode permettant de gérer les redirections et les besoin de rôles pour l'accès aux chemins. </b>
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.formLogin()
		        .loginPage("/login")
		        .failureUrl("/login-error")
	        .and()
		        .logout()
		        .logoutSuccessUrl("/index")
	        .and()
		        .authorizeRequests()
		        .antMatchers("/admin/**").hasRole("ADMIN")
		        .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
		        .antMatchers("/shared/**").hasAnyRole("USER","ADMIN")
	        .and()
		        .exceptionHandling()
		        .accessDeniedPage("/403");

    }

    /**
     * <b> Méthode permettant de gérer l'enregistrement des utilisateurs stoqués en base de données dans la configuration et de initialiser/lancer l'instance du thread ReloadKeyThread présent dans la class ApplicationEndHandle. </b>
     * <p> Si aucun utilisateur n'est stoqué en base de données, alors un seul compte sera enregistré : admin/admin. </p>
     * 
     * @see IUserService
     * @see AuthenticationManagerBuilder
     * @see ReloadKeyThread
     * @see ApplicationEndHandle
     */
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {   
    	if (!users.getRepository().findAll().isEmpty()) {
	        for (User u : users.getRepository().findAll()) {
	        	auth.inMemoryAuthentication()
		    		.withUser(u.getLogin())
		    		.password("{noop}" + encryption.decrypt(u.getPass()))
		    		.roles(u.getHeightRole().getLevel() > 0 ? "ADMIN" : "USER");
	        }
    	} else {
    		auth.inMemoryAuthentication()
	    		.withUser("admin")
	    		.password("{noop}" + "admin")
	    		.roles("ADMIN");
    	}
        auth.userDetailsService(inMemoryUserDetailsManager());
        ApplicationEndHandle.reloader = new ReloadKeyThread(encryption);
//        ApplicationEndHandle.reloader.start();
    }
    
    /**
     * <b> 
     * 	Permet de récupérer une instance de InMemoryUserDetailsManager contenant les utilisateurs déjà stoqués
     * 	dans la configuration. Utilisé dans le Controller UserController.
     * </b>
     * 
     * @return Une instance de InMemoryUserDetailsManager contenant les utilisateurs enregistrés et permettant de les gérer.
     * 
     * @see UserController
     */
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        final Properties users = new Properties();
        return new InMemoryUserDetailsManager(users);
    }
    
}