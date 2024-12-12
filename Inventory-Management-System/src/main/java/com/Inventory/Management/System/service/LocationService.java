package com.Inventory.Management.System.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.Inventory.Management.System.model.Warehouse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * LocationService provides functionality for determining warehouse proximity based on zip codes.
 * It integrates with the Google Distance Matrix API to calculate distances.
 */
@Service
public class LocationService {

    /**
     * The API key for accessing the Google Distance Matrix API.
     * This value is injected from the application's configuration properties.
     */
    @Value("${google.api.key}")  
    private String apiKey;

    /**
     * Returns a sorted list of warehouses based on their proximity to the provided order zip code.
     *
     * @param orderZipcode The zip code of the order's location.
     * @param warehouses   The list of warehouses to calculate distances for.
     * @return A list of warehouses sorted by proximity (closest first).
     */
    public List<Warehouse> getWarehousesByProximity(String orderZipcode, List<Warehouse> warehouses) {
        // Prepares the destination zip codes as a pipe-separated string.
        String destinations = warehouses.stream()
                .map(Warehouse::getZipcode)
                .collect(Collectors.joining("|"));

        // Builds the Google Distance Matrix API URL with the order's zip code and destination zip codes.
        String url = String.format(
            "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s&key=%s",
            orderZipcode, destinations, apiKey);

        // Makes an HTTP GET request to the API using RestTemplate.
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        // Parses the JSON response from the API.
        JSONObject jsonResponse = new JSONObject(response);

        // Validates the response structure to ensure data is available.
        if (!jsonResponse.has("rows") || jsonResponse.getJSONArray("rows").isEmpty()) {
            throw new RuntimeException("No data received from Distance Matrix API");
        }

        JSONArray rows = jsonResponse.getJSONArray("rows");
        JSONArray elements = rows.getJSONObject(0).optJSONArray("elements");

        if (elements == null || elements.isEmpty()) {
            throw new RuntimeException("No elements found in Distance Matrix API response");
        }

        // Extracts distances from the API response.
        List<Integer> distances = IntStream.range(0, elements.length())
                .mapToObj(i -> elements.getJSONObject(i).getJSONObject("distance").getInt("value"))
                .collect(Collectors.toList());

        // Sorts the warehouses based on the extracted distances.
        return IntStream.range(0, warehouses.size())
                .boxed()
                .sorted(Comparator.comparing(distances::get))  // Sort by distance (ascending).
                .map(warehouses::get)  // Map indices back to warehouses.
                .collect(Collectors.toList());
    }
}
