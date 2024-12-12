package com.Inventory.Management.System.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfig configures security settings for the application using Spring Security.
 * It defines how HTTP requests are secured and provides a password encoding mechanism.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain for the application.
     * 
     * - Enables CORS to allow cross-origin requests.
     * - Disables CSRF protection for simplicity (should be enabled for production).
     * - Allows all HTTP requests without authentication.
     *
     * @param http The HttpSecurity object for configuring security settings.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs while building the security configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()  // Enables Cross-Origin Resource Sharing.
            .csrf().disable()  // Disables CSRF protection (use with caution in production).
            .authorizeRequests()
            .anyRequest().permitAll();  // Allows all requests without authentication.

        return http.build();
    }

    /**
     * Defines a bean for encoding passwords using BCrypt.
     * BCrypt is a strong hashing function that is recommended for password storage.
     *
     * @return A PasswordEncoder instance using BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
