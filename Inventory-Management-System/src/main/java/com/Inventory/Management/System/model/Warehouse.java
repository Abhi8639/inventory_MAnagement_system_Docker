package com.Inventory.Management.System.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

/**
 * The Warehouse class represents an entity for warehouses in the inventory system.
 * Each warehouse contains details such as location, capacity, and associated stocks and users.
 */
@Entity
@Table(name = "warehouses")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Warehouse {

    /**
     * This is the unique identifier for the warehouse.
     * This ID is auto-generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id", nullable = false, unique = true)
    private String warehouseId;

    // The location of the warehouse.
    private String location;

    // The maximum capacity of the warehouse.
    private int capacity;

    // The postal code of the warehouse's location.
    private String zipcode;

    /**
     * The set of stock entries associated with this warehouse.
     * 
     * - `@OneToMany` defines a one-to-many relationship with the `Stock` entity.
     * - `mappedBy` indicates that the `warehouse` field in `Stock` owns the relationship.
     * - `@JsonIgnore` prevents serialization of the `stocks` field to avoid circular references.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "warehouse")
    private Set<Stock> stocks;

    /**
     * The set of users associated with this warehouse.
     * 
     * - `@OneToMany` defines a one-to-many relationship with the `User` entity.
     * - `mappedBy` indicates that the `warehouse` field in `User` owns the relationship.
     * - `cascade = CascadeType.ALL` ensures that changes to the warehouse are cascaded to its associated users.
     * - `@JsonIgnore` prevents serialization of the `users` field to avoid circular references.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<User> users;

    // Constructors

    // Default constructor.
    public Warehouse() {}

    /**
     * Constructor with warehouseId.
     *
     * @param warehouseId The unique identifier of the warehouse.
     */
    public Warehouse(String warehouseId2) {
        this.warehouseId = warehouseId2;
    }

    // Getters and Setters

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Set<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(Set<Stock> stocks) {
        this.stocks = stocks;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
