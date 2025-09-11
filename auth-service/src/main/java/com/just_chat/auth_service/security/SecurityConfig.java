// src/main/java/com/justchat/security/SecurityConfig.java
package com.just_chat.auth_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain security(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(a -> a
                        .anyRequest().authenticated() // everything requires login
                )
                .oauth2Login(o -> o.defaultSuccessUrl("/me", true) // after login, go to /me
                );
        return http.build();
    }
}
