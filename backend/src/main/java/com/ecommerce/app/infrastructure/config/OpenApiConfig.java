package com.ecommerce.app.infrastructure.config;

import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
    	
    	final String securitySchemeName = "bearerAuth";
    	
        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce API")
                        .version("1.0")
                        .description("Documentación de la API para el sistema E-Commerce — fork de Pardos06")
                        .contact(new Contact()
                                .name("Roddorr2")
                                .email("rodricu0702@gmail.com")
                                .url("https://github.com/Roddorr2/ECommerce-SW"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                		.addSecuritySchemes(securitySchemeName,
                				new SecurityScheme()
                					.name(securitySchemeName)
                					.type(SecurityScheme.Type.HTTP)
                					.scheme("bearer")
                					.bearerFormat("JWT")
                		)
        		);
    }
}
