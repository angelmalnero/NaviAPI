package com.example.navidadapi.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Aplica CORS a todas las rutas de la aplicación (/**)
        registry.addMapping("/**")
                // Permite solicitudes desde cualquier origen.
                // Para producción, reemplaza "*" con los dominios específicos de tu frontend.
                .allowedOrigins("*")
                // Define qué métodos HTTP están permitidos
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // Permite el uso de credenciales (cookies, headers de autenticación)
                .allowCredentials(true);
    }
}