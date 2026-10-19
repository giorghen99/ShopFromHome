package com.example.backend_shopfromhome.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Configurazione globale di CORS per tutte le richieste
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Consenti richieste da ogni origine
        registry.addMapping("/**") // Applica a tutte le rotte
                .allowedOrigins("http://localhost:3000", "http://192.168.56.1:8080", "com.example.shopfromhome") // Sostituisci con il dominio del tuo frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE") // I metodi consentiti
                .allowedHeaders("*") // Tutti gli header sono consentiti
                .allowCredentials(true); // Se hai bisogno di inviare cookie
    }
}
