package com.Inventory.Management.System.controller;

import com.Inventory.Management.System.model.*;
import com.Inventory.Management.System.service.OrderService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * OrderController handles RESTful endpoints for managing orders.
 * It provides functionality to create new orders and retrieve existing orders.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    // Injecting the OrderService to handle the business logic for order management.
    @Autowired
    private OrderService orderService;

    /**
     * It is the Endpoint to create a new order.
     * This method accepts an Order object as a JSON payload, processes it,
     * and saves it using the service layer.
     *
     * @param order The Order object to be created.
     * @return ResponseEntity containing the saved Order object if successful,
     *         or an error message with an appropriate HTTP status code if something goes wrong.
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            // Logs the received order for debugging purposes.
            System.out.println("Received Order: " + order);
            
            // Saves the order using the OrderService.
            Order savedOrder = orderService.createOrder(order);
            
            // Returns a 201 Created response with the saved order.
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        } catch (IllegalArgumentException e) {
            // Returns a 400 Bad Request response if the input data is invalid.
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Returns a 500 Internal Server Error response for unexpected errors.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error occurred while creating the order");
        }
    }

    /**
     * It is the Endpoint to retrieve all orders.
     * This method returns a list of all orders stored in the system.
     *
     * @return ResponseEntity containing the list of Order objects.
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Order>> getAllOrders() {
        // Fetches all orders using the OrderService.
        List<Order> orders = orderService.getAllOrders();
        
        // Returns a 200 OK response with the list of orders.
        return ResponseEntity.ok(orders);
    }

}
