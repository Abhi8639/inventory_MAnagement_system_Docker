package com.Inventory.Management.System.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

/**
 * This Product class represents an entity for products in the inventory system.
 * Each product has details such as name, price, and overall quantity.
 * It is associated with multiple stocks across warehouses.
 */
@Entity
@Table(name = "products")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {

    /**
     * This is the unique identifier for the product.
     * This ID is auto-generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false, unique = true)
    private String productId;

    // The name of the product.
    private String name;

    //The price of the product.
    private double price;

    // The overall quantity of the product across all warehouses.

    private int overall_quantity;

    /**
     * The set of stock entries associated with this product.
     * 
     * - `@OneToMany` defines the one-to-many relationship with the `Stock` entity.
     * - `mappedBy` indicates that the `product` field in `Stock` owns the relationship.
     * - `@JsonIgnore` prevents serialization of the `stocks` field to avoid circular references.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private Set<Stock> stocks;

    // Constructors

    // Default constructor.

    public Product() {}

    /**
     * Constructor with productId.
     *
     * @param productId The unique identifier of the product.
     */
    public Product(String productId2) {
        this.productId = productId2;
    }

    // Getters and Setters

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getOverallQuantity() {
        return overall_quantity;
    }

    public void setOverallQuantity(int overall_quantity) {
        this.overall_quantity = overall_quantity;
    }

    public Set<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(Set<Stock> stocks) {
        this.stocks = stocks;
    }

    /**
     * It Provides a string representation of the Product object for debugging purposes.
     *
     * @return A string containing product details.
     */
    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", overallQuantity=" + overall_quantity +
                '}';
    }
}
