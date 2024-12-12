package com.Inventory.Management.System.service;

import com.Inventory.Management.System.model.Product;
import com.Inventory.Management.System.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ProductService handles business logic for managing products, including adding, updating,
 * retrieving, and deleting products in the inventory system.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Adds a new product to the inventory.
     *
     * @param product The product to be added.
     * @return The saved product.
     */
    public Product addProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        System.out.println("Product added: " + savedProduct);
        return savedProduct;
    }

    /**
     * Updates the overall quantity of a product by adding the specified amount.
     *
     * @param productId The ID of the product to update.
     * @param quantity  The quantity to be added (can be negative for subtraction).
     * @return The updated product.
     * @throws RuntimeException If the product is not found.
     */
    public Product updateOverallQuantity(String productId, int quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setOverallQuantity(product.getOverallQuantity() + quantity);
        Product updatedProduct = productRepository.save(product);
        System.out.println("Product quantity updated: " + updatedProduct);
        return updatedProduct;
    }

    /**
     * Updates the details of a product, such as name, price, and overall quantity.
     *
     * @param productId      The ID of the product to update.
     * @param productDetails The product details to be updated.
     * @return The updated product.
     * @throws RuntimeException If the product is not found.
     */
    public Product updateProductDetails(String productId, Product productDetails) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setOverallQuantity(productDetails.getOverallQuantity());
        Product updatedProduct = productRepository.save(product);
        System.out.println("Product updated: " + updatedProduct);
        return updatedProduct;
    }

    /**
     * Retrieves a list of all products in the inventory.
     *
     * @return A list of products.
     */
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        System.out.println("Fetched products: " + products);
        return products;
    }

    /**
     * Deletes a product from the inventory by its ID.
     *
     * @param productId The ID of the product to be deleted.
     */
    public void deleteProduct(String productId) {
        productRepository.deleteById(productId);
        System.out.println("Product deleted with ID: " + productId);
    }
}
