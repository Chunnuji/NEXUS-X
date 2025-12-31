package com.example.common.config;

import com.example.common.security.AuthUtil;
import com.example.common.security.SharedJwtFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AutoConfiguration
@EnableWebSecurity
@EnableMethodSecurity
public class SharedSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        // Returns an empty manager so Spring doesn't create the default one with a password
        return username -> {
            throw new UsernameNotFoundException("User not found");
        };
    }

    @Bean
    public AuthUtil authUtil() {
        return new AuthUtil();
    }

    @Bean
    public SharedJwtFilter sharedJwtFilter(AuthUtil authUtil) {
        return new SharedJwtFilter(authUtil);
    }

    @Bean
    @Primary
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SharedJwtFilter filter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**", "/public/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}