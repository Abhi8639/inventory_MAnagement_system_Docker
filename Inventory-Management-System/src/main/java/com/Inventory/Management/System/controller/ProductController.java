package com.Inventory.Management.System.controller;

import com.Inventory.Management.System.model.Product;
import com.Inventory.Management.System.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProductController handles RESTful endpoints for managing products in the inventory system.
 * It provides functionality to add, update, retrieve, and delete products.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    // Injecting the ProductService to handle business logic for product management.
    @Autowired
    private ProductService productService;

    /**
     * It is the Endpoint to add a new product.
     * Accepts product details in the request body and creates a new product.
     *
     * @param product The Product object containing details of the product to be added.
     * @return ResponseEntity containing the created Product object.
     */
    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product createdProduct = productService.addProduct(product);
        return ResponseEntity.ok(createdProduct);
    }

    /**
     * It is the Endpoint to update the overall quantity of a product.
     * Updates the quantity of a specific product identified by its ID.
     *
     * @param productId The ID of the product to update.
     * @param quantity  The new quantity to be set for the product.
     * @return ResponseEntity containing the updated Product object.
     */
    @PutMapping("/update-quantity/{productId}")
    public ResponseEntity<Product> updateOverallQuantity(@PathVariable String productId, @RequestParam int quantity) {
        Product updatedProduct = productService.updateOverallQuantity(productId, quantity);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * It is the  Endpoint to retrieve all products.
     * Returns a list of all products in the inventory.
     *
     * @return ResponseEntity containing a list of Product objects.
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * It is the Endpoint to update product details.
     * Updates the details of a specific product identified by its ID.
     *
     * @param productId      The ID of the product to update.
     * @param productDetails The Product object containing updated product details.
     * @return ResponseEntity containing the updated Product object.
     */
    @PutMapping("/update/{productId}")
    public ResponseEntity<Product> updateProductDetails(
        @PathVariable String productId, 
        @RequestBody Product productDetails
    ) {
        Product updatedProduct = productService.updateProductDetails(productId, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * It is the Endpoint to delete a product.
     * Deletes a specific product identified by its ID.
     *
     * @param productId The ID of the product to delete.
     * @return ResponseEntity with no content to indicate successful deletion.
     */
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }
}
