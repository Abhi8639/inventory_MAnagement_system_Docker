package com.Inventory.Management.System.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * GooglePlacesService provides functionality to interact with the Google Places API.
 * It is primarily used for fetching location suggestions (e.g., zip codes or regions) based on user input.
 */
@Service
public class GooglePlacesService {

    /**
     * The API key for accessing the Google Places API.
     * This value is injected from the application's configuration properties.
     */
    @Value("${google.api.key}")
    private String googleApiKey;

    //The base URL for the Google Places API autocomplete endpoint.
    private static final String GOOGLE_PLACES_URL = 
        "https://maps.googleapis.com/maps/api/place/autocomplete/json?input={input}&types=(regions)&key={apiKey}";

    /**
     * Fetches location suggestions (e.g., zip codes or regions) based on the given input.
     *
     * @param input The user-provided input to search for location suggestions.
     * @return A Map containing the API response, which includes location suggestions.
     */
    public Map<String, Object> fetchZipSuggestions(String input) {
        // Creates a RestTemplate instance for making HTTP requests.
        RestTemplate restTemplate = new RestTemplate();

        // Defines request parameters to be passed to the API.
        Map<String, String> params = new HashMap<>();
        params.put("input", input);
        params.put("apiKey", googleApiKey);

        // Makes a GET request to the Google Places API and return the response as a Map.
        return restTemplate.getForObject(GOOGLE_PLACES_URL, Map.class, params);
    }
}
