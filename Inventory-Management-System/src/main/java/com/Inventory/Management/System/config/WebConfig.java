package com.Inventory.Management.System.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * WebConfig class is a Spring configuration class that sets up Cross-Origin Resource Sharing (CORS)
 * settings for the application. This allows the backend to accept requests from specific origins.
 */
@Configuration
public class WebConfig {

    /**
     * Configures and returns a CORS filter bean.
     * This filter is responsible for handling CORS-related requests based on the defined rules.
     *
     * @return a CorsFilter configured to manage CORS settings.
     */
    @Bean
    public CorsFilter corsFilter() {
        // Creates a source for registering CORS configuration.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Creates a CORS configuration instance to define specific rules.
        CorsConfiguration config = new CorsConfiguration();
        
        // Allows credentials (e.g., cookies or HTTP authentication) to be included in requests.
        config.setAllowCredentials(true);
        
        // Allows requests from a specific origin (e.g., React frontend running on localhost).
        config.addAllowedOrigin("http://localhost:3000");
        
        // Allows all headers in the request.
        config.addAllowedHeader("*");
        
        // Allows all HTTP methods (GET, POST, PUT, DELETE, etc.).
        config.addAllowedMethod("*");
        
        // Applies the configuration to all endpoints (/**).
        source.registerCorsConfiguration("/**", config);

        // Returns a new CorsFilter instance with the defined configuration.
        return new CorsFilter(source);
    }
}
