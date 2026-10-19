package com.example.backend_shopfromhome.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/**").permitAll()  // API pubbliche
                        .anyRequest().permitAll()  // Tutte le altre richieste sono permesse
                )
                .csrf(csrf -> csrf.disable());  // Disabilita la protezione CSRF (se non necessaria)
        // Non è necessario configurare 'httpBasic()', 'formLogin()' o 'logout()' in questo caso
        return http.build();
    }
}
