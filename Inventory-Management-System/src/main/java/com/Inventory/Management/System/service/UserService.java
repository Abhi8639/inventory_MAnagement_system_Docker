package com.Inventory.Management.System.service;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Inventory.Management.System.model.User;
import com.Inventory.Management.System.model.Warehouse;
import com.Inventory.Management.System.repository.UserRepository;
import com.Inventory.Management.System.repository.WarehouseRepository;

/**
 * UserService handles business logic related to user management, including
 * registration, retrieval, and saving user details.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new user, encrypts their password, assigns them to a warehouse,
     * and sets their account status to "Active".
     *
     * @param user        The user to be registered.
     * @param warehouseId The ID of the warehouse to associate with the user.
     * @return The saved user entity.
     * @throws RuntimeException If the warehouse ID is invalid.
     */
    public User registerUser(User user, String warehouseId) {
        // Encrypts the user's password before saving.
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Sets creation timestamp and default account status.
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setAccountStatus("Active");

        // Retrieves and assign the warehouse to the user.
        Optional<Warehouse> warehouseOptional = warehouseRepository.findById(warehouseId);
        if (warehouseOptional.isPresent()) {
            user.setWarehouse(warehouseOptional.get());
        } else {
            throw new RuntimeException("Invalid Warehouse ID");
        }

        // Saves and return the user.
        return userRepository.save(user);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email The email of the user.
     * @return An Optional containing the user if found, or empty if not.
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Saves or updates a user in the database.
     *
     * @param user The user entity to be saved or updated.
     * @return The saved user entity.
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
