package com.sb.products.main.config.global;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${cors.origin-patterns}")
	private String corsPatterns;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		var allowedOrigins = corsPatterns.split(",");
		registry.addMapping("/**")
		  .allowedMethods("*")
		  .allowedOrigins(allowedOrigins)
		  .allowCredentials(true);
	}
}
