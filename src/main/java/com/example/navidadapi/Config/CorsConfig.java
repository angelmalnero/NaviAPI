package com.example.navidadapi.Config; // Usa el paquete que corresponda

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a todos los endpoints de la API
                .allowedOrigins("*") // ⚠️ Permite CUALQUIER origen. Para producción, cámbialo al dominio de tu cliente.
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite todos los métodos necesarios
                .allowedHeaders("*"); // Permite todos los headers
    }
}