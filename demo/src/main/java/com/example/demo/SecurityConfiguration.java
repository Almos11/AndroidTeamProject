package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfiguration {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           TokenAuthenticationFilter tokenFilter) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/**").permitAll();
        http.addFilterBefore(tokenFilter, AuthorizationFilter.class);
        http.csrf().disable();
        return http.build();
    }
}