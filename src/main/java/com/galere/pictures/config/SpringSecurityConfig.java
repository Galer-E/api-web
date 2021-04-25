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
import com.galere.pictures.entities.User;
import com.galere.pictures.services.IEncryptionService;
import com.galere.pictures.services.IUserService;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private IUserService users;
	
	@Autowired
	private IEncryptionService encryption;
	
    public SpringSecurityConfig() {
        super();
    }

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
    
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        final Properties users = new Properties();
        return new InMemoryUserDetailsManager(users);
    }
    
}