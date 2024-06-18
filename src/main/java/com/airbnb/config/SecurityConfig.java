package com.airbnb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {

    private final JWTRequestFilter jwtRequestFilter;

    public SecurityConfig(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().and()
                .addFilterBefore(jwtRequestFilter, AuthorizationFilter.class)
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/users/addUser", "/api/v1/users/login").permitAll()
                .requestMatchers("/api/v1/messages/**").permitAll() // Allow unauthenticated access to all message endpoints
                .requestMatchers("/api/v1/users/profile").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated();
        return http.build();
    }
}
