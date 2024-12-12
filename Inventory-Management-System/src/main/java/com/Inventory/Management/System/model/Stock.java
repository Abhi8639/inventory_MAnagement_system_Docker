package com.Inventory.Management.System.model;

import jakarta.persistence.*;

/**
 * The Stock class represents an entity for managing stock levels in a warehouse.
 * It links products to warehouses and tracks their quantities.
 */
@Entity
@Table(name = "stock")
public class Stock {

    /**
     * This is the unique identifier for the stock entry.
     * This ID is auto-generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id", nullable = false, unique = true)
    private String stock_id;

    /**
     * The warehouse where this stock entry is located.
     * 
     * - `@ManyToOne` defines a many-to-one relationship with the `Warehouse` entity.
     * - `@JoinColumn` specifies the foreign key column `warehouse_id`.
     * - `nullable = false` enforces that each stock entry must be associated with a warehouse.
     */
    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    /**
     * The product associated with this stock entry.
     * 
     * - `@ManyToOne` defines a many-to-one relationship with the `Product` entity.
     * - `@JoinColumn` specifies the foreign key column `product_id`.
     * - `nullable = false` enforces that each stock entry must be associated with a product.
     */
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // The quantity of the product in the specified warehouse.

    private int quantity;

    // Getters and setters

    public String getStockId() {
        return stock_id;
    }

    public void setStockId(String stock_id) {
        this.stock_id = stock_id;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * It provides a string representation of the Stock object for debugging purposes.
     *
     * @return A string containing stock details.
     */
    @Override
    public String toString() {
        return "Stock{" +
                "stock_id='" + stock_id + '\'' +
                ", warehouse=" + warehouse +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
