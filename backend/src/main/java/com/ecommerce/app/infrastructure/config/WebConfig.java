package com.ecommerce.app.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String rutaImagenes = "file:" + Paths.get(
                System.getProperty("user.dir"),
                 "wwwroot", "imagenes"
        ).toString() + "/";

        registry.addResourceHandler("/imagenes/**")
                .addResourceLocations(rutaImagenes)
                .setCachePeriod(0);
    }
}