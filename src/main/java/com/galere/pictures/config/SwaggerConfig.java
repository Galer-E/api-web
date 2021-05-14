package com.galere.pictures.config;

import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
 
@EnableSwagger2
@Configuration
public class SwaggerConfig {
 
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Galer-E")
				.description("Bibliothèque d'image")
				.version("1.0")
				.build();
	}
 
}
