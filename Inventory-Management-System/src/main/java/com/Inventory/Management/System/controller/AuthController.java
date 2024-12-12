package com.Inventory.Management.System.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.Inventory.Management.System.model.User;
import com.Inventory.Management.System.service.UserService;

/**
 * AuthController handles authentication-related operations such as user registration and login.
 * It exposes RESTful endpoints to interact with the authentication system.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://127.0.0.1:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class AuthController {

    // Injecting the UserService to manage user-related operations.
    @Autowired
    private UserService userService;

    // Injecting PasswordEncoder to securely encode and verify passwords.
    @Autowired
    private PasswordEncoder passwordEncoder; 

    /**
     * It is the Endpoint for registering a new user.
     * This method takes a JSON payload with user details, creates a new user,
     * and associates it with a specific warehouse if provided.
     *
     * @param userData Map containing user details (name, email, password, role, warehouseId).
     * @return ResponseEntity containing the created User object.
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody Map<String, Object> userData) {
        // Extracts user details from the request body.
        String name = (String) userData.get("name");
        String email = (String) userData.get("email");
        String password = (String) userData.get("password");
        String role = (String) userData.get("role");
        String warehouseId = (String) userData.get("warehouseId");  

        // Creates a new User object and set its properties.
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password); 
        user.setRole(role);
        user.setAccountStatus("Active");

        // Registers the user through the UserService and associate it with a warehouse if provided.
        User createdUser = userService.registerUser(user, warehouseId);
        
        // Returns the created user with a 200 OK response.
        return ResponseEntity.ok(createdUser);
    }

    /**
     * It is the Endpoint for logging in a user.
     * This method validates the user's credentials and, if successful,
     * returns the user's role and warehouse information.
     *
     * @param loginRequest Map containing user login details (email and password).
     * @return ResponseEntity containing login status, role, and warehouseId if successful,
     *         or an error message if authentication fails.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
        // Extracts email and password from the request body.
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        // Finds the user by email.
        Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Verifies the provided password against the stored hashed password.
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Retrieves the warehouse ID if the user is associated with a warehouse.
                String warehouseId = user.getWarehouse() != null ? user.getWarehouse().getWarehouseId() : null;

                // Creates a response map with login details.
                Map<String, Object> response = Map.of(
                    "login", "true",
                    "role", user.getRole(),
                    "warehouseId", warehouseId  
                );

                // Returns a 200 OK response with the login details.
                return ResponseEntity.ok(response);
            } else {
                // Returns a 401 Unauthorized response if the password is invalid.
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }
        } else {
            // Returns a 401 Unauthorized response if the user is not found.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
    }
}
