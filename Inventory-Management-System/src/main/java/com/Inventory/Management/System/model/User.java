package com.Inventory.Management.System.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

/**
 * The User class represents an entity for users in the inventory system.
 * It includes details such as name, email, role, and association with a warehouse.
 */
@Entity
@Table(name = "Users")
public class User {

    /**
     * This is the unique identifier for the user.
     * This ID is auto-generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    //The name of the user.
    @Column(nullable = false)
    private String name;

    // The email address of the user.
    @Column(nullable = false, unique = true)
    private String email;

    // The encrypted password of the user.
    @Column(nullable = false)
    private String password;

    // The account status of the user (e.g., Active, Inactive).

    private String accountStatus;

    //The timestamp when the user was created.

    private Timestamp createdAt;

    // The timestamp when the user was last updated.

    private Timestamp updatedAt;

    // The role of the user (e.g., Admin, Manager, Employee).

    @Column(nullable = false)
    private String role;

    /**
     * The warehouse associated with the user.
     * 
     * - `@ManyToOne` defines a many-to-one relationship with the `Warehouse` entity.
     * - `fetch = FetchType.LAZY` ensures that the warehouse details are loaded lazily.
     * - `@JoinColumn` specifies the foreign key column in the `Users` table linking to the `Warehouse` table.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", referencedColumnName = "warehouse_id")
    private Warehouse warehouse;

    // Getters and Setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}
