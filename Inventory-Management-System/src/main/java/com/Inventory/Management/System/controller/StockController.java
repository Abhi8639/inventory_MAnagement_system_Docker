package com.Inventory.Management.System.controller;

import com.Inventory.Management.System.model.Stock;
import com.Inventory.Management.System.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * StockController handles RESTful endpoints for managing stock in the inventory system.
 * It provides functionality to view, update, and add or update stock levels for products in specific warehouses.
 */
@RestController
@RequestMapping("/api/stock")
public class StockController {

    // Injecting the StockService to handle stock-related business logic.
    @Autowired
    private StockService stockService;

    /**
     * It is the Endpoint to get all stock items for a specific warehouse.
     * Retrieves a list of Stock objects based on the warehouse ID.
     *
     * @param warehouseId The ID of the warehouse.
     * @return ResponseEntity containing a list of Stock objects for the specified warehouse.
     */
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<Stock>> getProductsByWarehouse(@PathVariable String warehouseId) {
        List<Stock> stocks = stockService.getProductsByWarehouse(warehouseId);
        return ResponseEntity.ok(stocks);
    }

    /**
     * It is the Endpoint to update the stock quantity for a specific product in a specific warehouse.
     * Accepts a JSON payload containing the product ID, warehouse ID, and the new quantity.
     *
     * @param requestData A map containing "productId", "warehouseId", and "quantity".
     * @return ResponseEntity containing the updated Stock object, or an error response for invalid data.
     */
    @PutMapping("/update")
    public ResponseEntity<Stock> updateStock(@RequestBody Map<String, Object> requestData) {
        // Extracts parameters from the request data.
        String productId = (String) requestData.get("productId");
        String warehouseId = (String) requestData.get("warehouseId");

        Integer quantity;
        try {
            // Parses quantity and handle potential number format errors.
            quantity = Integer.parseInt(requestData.get("quantity").toString());
        } catch (NumberFormatException e) {
            // Returns 400 Bad Request if quantity is not a valid integer.
            return ResponseEntity.badRequest().body(null);
        }

        // Updates the stock quantity and return the updated stock object.
        Stock updatedStock = stockService.updateStockQuantity(productId, warehouseId, quantity);
        if (updatedStock != null) {
            return ResponseEntity.ok(updatedStock);
        }
        // Returns 400 Bad Request if the update fails.
        return ResponseEntity.badRequest().build();
    }

    /**
     * It is the Endpoint to add or update stock for a specific product in a specific warehouse.
     * Accepts a JSON payload containing the product ID, warehouse ID, and the quantity.
     *
     * @param requestData A map containing "productId", "warehouseId", and "quantity".
     * @return ResponseEntity containing the added or updated Stock object, or an error response for invalid data.
     */
    @PostMapping("/addOrUpdate")
    public ResponseEntity<Stock> addOrUpdateStock(@RequestBody Map<String, Object> requestData) {
        // Extracts parameters from the request data.
        String productId = (String) requestData.get("productId");
        String warehouseId = (String) requestData.get("warehouseId");

        Integer quantity;
        try {
            // Parses quantity and handle potential number format errors.
            quantity = Integer.parseInt(requestData.get("quantity").toString());
        } catch (NumberFormatException e) {
            // Returns 400 Bad Request if quantity is not a valid integer.
            return ResponseEntity.badRequest().body(null);
        }

        // It will Add or update the stock and return the stock object.
        Stock stock = stockService.addOrUpdateStock(productId, warehouseId, quantity);
        if (stock != null) {
            return ResponseEntity.ok(stock);
        }
        // Returns 400 Bad Request if the operation fails.
        return ResponseEntity.badRequest().build();
    }

}
