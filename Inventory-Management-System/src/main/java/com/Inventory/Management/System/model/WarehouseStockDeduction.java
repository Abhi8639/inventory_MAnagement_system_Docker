package com.Inventory.Management.System.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * The WarehouseStockDeduction class represents an entity for tracking stock deductions
 * in a warehouse, usually associated with fulfilling an order.
 */
@Entity
@Table(name = "warehousestockdeduction")
public class WarehouseStockDeduction {

    /**
     * This is the unique identifier for the stock deduction record.
     * This ID is auto-generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deduction_id", nullable = false, unique = true)
    private String deductionId;

    // The ID of the order associated with this stock deduction.
    @Column(name = "order_id", nullable = false)
    private String orderId;

    // The ID of the product for which stock was deducted.
    @Column(name = "product_id", nullable = false)
    private String productId;

    // The ID of the warehouse where the stock was deducted.
    @Column(name = "warehouse_id", nullable = false)
    private String warehouseId;

    // The quantity of stock deducted for the product.
    @Column(name = "quantity_deducted", nullable = false)
    private int quantityDeducted;

    /**
     * The timestamp indicating when the deduction occurred.
     * Defaults to the current time when the object is created.
     */
    @Column(name = "deduction_timestamp", nullable = false)
    private LocalDateTime deductionTimestamp = LocalDateTime.now();

    /**
     * The product entity associated with this stock deduction.
     * 
     * - `@ManyToOne` defines a many-to-one relationship with the `Product` entity.
     * - `@JoinColumn` links this entity to the `product` table via the `product_id` column.
     * - `insertable = false` and `updatable = false` prevent this relationship from overriding
     *   the `product_id` column directly, as it's managed by the database.
     */
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
    private Product product;

    // Getters and Setters

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getDeductionId() {
        return deductionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getQuantityDeducted() {
        return quantityDeducted;
    }

    public void setQuantityDeducted(int quantityDeducted) {
        this.quantityDeducted = quantityDeducted;
    }

    public LocalDateTime getDeductionTimestamp() {
        return deductionTimestamp;
    }

    public void setDeductionTimestamp(LocalDateTime deductionTimestamp) {
        this.deductionTimestamp = deductionTimestamp;
    }
}
