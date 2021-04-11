package com.galere.pictures.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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
		        .antMatchers("/user/**").hasRole("USER")
		        .antMatchers("/shared/**").hasAnyRole("USER","ADMIN")
	        .and()
		        .exceptionHandling()
		        .accessDeniedPage("/403");

    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {        
        for (User u : users.getRepository().findAll()) {
        	auth.inMemoryAuthentication()
	    		.withUser(u.getLogin())
	    		.password("{noop}" + encryption.decrypt(u.getPass()))
	    		.roles(u.getHeightRole().getLevel() > 0 ? "ADMIN" : "USER");
        }
    }


}