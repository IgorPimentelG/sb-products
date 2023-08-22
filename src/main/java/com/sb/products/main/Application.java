package com.sb.products.main;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.sb.products.infra", "com.sb.products.main"})
@EnableJpaRepositories("com.sb.products.infra.database.repositories")
@EntityScan("com.sb.products.infra.database.schemas")
@OpenAPIDefinition(info = @Info(title = "SB Products", version = "1.0.0"))
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
