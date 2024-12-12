package com.Inventory.Management.System.controller;

import com.Inventory.Management.System.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * GooglePlacesController handles requests related to fetching location suggestions
 * using the Google Places API. This is useful for auto-completing zip codes or locations.
 */
@RestController
@RequestMapping("/api/places")
public class GooglePlacesController {

    // Injecting the GooglePlacesService to handle business logic related to location suggestions.
    @Autowired
    private GooglePlacesService googlePlacesService;

    /**
     * It is the Endpoint to fetch zip code or location suggestions based on user input.
     * It communicates with the Google Places API through the service layer.
     *
     * @param input A query string for location suggestions (e.g., partial zip code or address).
     * @return ResponseEntity containing location suggestions if input is valid,
     *         or a bad request response if the input is missing or invalid.
     */
    @GetMapping
    public ResponseEntity<?> getZipSuggestions(@RequestParam String input) {
        // Validates that the input query is not null or empty.
        if (input == null || input.isEmpty()) {
            // Returns a 400 Bad Request response if the input is invalid.
            return ResponseEntity.badRequest().body("Input query is required.");
        }

        // Fetches location suggestions from the GooglePlacesService based on the input.
        Map<String, Object> suggestions = googlePlacesService.fetchZipSuggestions(input);

        // Returns a 200 OK response containing the suggestions.
        return ResponseEntity.ok(suggestions);
    }
}
