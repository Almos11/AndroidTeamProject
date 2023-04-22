package com.example.demo.config;

import com.example.demo.components.TokenAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableScheduling
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