package com.redoca2k.cards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@SpringBootApplication
@OpenAPIDefinition(info = @Info(
	title = "Accounts microservice REST API Documentation", 
	description = "EazyBank Cards microservice REST API Documentation",
	version = "v1",
	contact = @Contact(name = "Afshan Ahmed Khan", email = "afshanahmeda2k@gmail.com", url = "https://www.eazybank.com"), 
	license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")), 
	externalDocs = @ExternalDocumentation(description = "EazyBank Accounts microservice Wiki Documentation", url = "https://www.eazybank.com/wiki")
)
public class CardsApplication {
	public static void main(String[] args) {
		SpringApplication.run(CardsApplication.class, args);
	}

}
