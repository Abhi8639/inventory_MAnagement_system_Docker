package com.Inventory.Management.System.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

/**
 * The Order class represents an entity for orders in the inventory system.
 * Each order contains details such as client's email, mobile number, address, and associated order items.
 */
@Entity
@Table(name = "orders")
public class Order {

    /**
     * This is the unique identifier for the order.
     * This ID is auto-generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;

    //The email address of the client who placed the order.
    private String email;

    //The mobile number of the client.

    private String mobileNo;

    // The address to which the order will be delivered.

    private String address;

    // The postal code associated with the delivery address.
    private String zipcode;

    /**
     * The list of order items associated with this order.
     * 
     * - `@OneToMany` defines the one-to-many relationship with `OrderItem`.
     * - `mappedBy` indicates that the `order` field in `OrderItem` owns the relationship.
     * - `cascade` ensures that all related `OrderItem` entities are persisted, updated, or deleted with this `Order`.
     * - `orphanRemoval` ensures that orphaned `OrderItem` entities (not associated with any `Order`) are removed.
     * - `@JsonManagedReference` helps manage bidirectional serialization with `OrderItem`.
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    // Getters and setters

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
