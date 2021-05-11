package com.galere.pictures.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * <b>
 * 	Class de configuration. Permet de gérer l'emplacement des différentes ressources de l'application.
 * </b>
 * <p> Les emplacements de ressources gérés : </p>
 * 	<ul>
 * 		<li> '/images/**' pointe vers l'emplacement 'classpath:/static/images/'. </li>
 * 		<li> '/css/**' pointe vers l'emplacement 'classpath:/static/css/'. </li>
 * 		<li> '/js/**' pointe vers l'emplacement 'classpath:/static/js/'. </li>
 * 		<li> '/webjars/**' pointe vers l'emplacement 'classpath:/META-INF/resources/webjars/'. </li>
 * 	</ul>
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
@SuppressWarnings("deprecation")
@Configuration
@EnableWebMvc
@ComponentScan
public class SpringWebConfig
        extends WebMvcConfigurerAdapter implements ApplicationContextAware {

    @SuppressWarnings("unused")
	private ApplicationContext applicationContext;

    /**
     * <b> Constructeur de cette class. </b>
     */
    public SpringWebConfig() {
        super();
    }
    
    /**
     * <b> Permet de définir l'instance de ApplicationContext. </b>
     * 
     * @param applicationContext L'instance de ApplicationContext.
     * 
     * @see ApplicationContext
     */
    public void setApplicationContext(final ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * <b> Méthode permettant de gérer l'emplacement des ressources de l'application. </b>
     * 
     * @see SpringWebConfig
     * @see ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }

}
