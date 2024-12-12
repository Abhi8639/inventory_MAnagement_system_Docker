package com.Inventory.Management.System.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

/**
 * The OrderItem class represents an entity for items in an order.
 * Each item is linked to a specific order and contains details such as product ID and quantity.
 */
@Entity
@Table(name = "order_items")
public class OrderItem {

    /**
     * This is the unique identifier for the order item.
     * This ID is auto-generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id", nullable = false, unique = true)
    private String orderItemId;

    //The ID of the product associated with this order item.

    @Column(name = "product_id")
    private String productId;

    /**
     * The order to which this item belongs.
     * 
     * - `@ManyToOne` defines the many-to-one relationship with the `Order` entity.
     * - `fetch = FetchType.LAZY` ensures that the associated order is loaded lazily.
     * - `@JoinColumn` specifies the foreign key column (`order_id`) for this relationship.
     * - `@JsonBackReference` prevents circular references during JSON serialization.
     */
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    //The quantity of the product in this order item.

    @Column(name = "quantity", nullable = false)
    private int quantity;

    // Getters and setters

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * It Provides a string representation of the OrderItem object for debugging purposes.
     *
     * @return A string containing order item details.
     */
    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId='" + orderItemId + '\'' +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
