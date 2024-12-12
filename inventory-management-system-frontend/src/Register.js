import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './Register.css';

/**
 * Register component allows new users to register by providing their details,
 * selecting a role, and associating themselves with a warehouse.
 */
function Register() {
    // State for user input fields
    const [name, setName] = useState(''); // User's name
    const [email, setEmail] = useState(''); // User's email
    const [password, setPassword] = useState(''); // User's password

    // State for managing warehouses and selected warehouse
    const [warehouses, setWarehouses] = useState([]); // List of warehouses fetched from the backend
    const [selectedWarehouse, setSelectedWarehouse] = useState(''); // Warehouse selected by the user

    // State for user role selection
    const [role, setRole] = useState('Manager'); // Default role is 'Manager'

    /**
     * Fetches the list of warehouses when the component mounts.
     * Uses Axios to make an API request and updates the `warehouses` state.
     */
    useEffect(() => {
        const fetchWarehouses = async () => {
            try {
                const response = await axios.get('/api/warehouses');
                setWarehouses(response.data); // Populates warehouses list
            } catch (error) {
                console.error('Error fetching warehouses:', error); // Logs any errors
            }
        };

        fetchWarehouses(); // Calls the function to fetch warehouses
    }, []);

    /**
     * Handles the form submission for registering a user.
     * Sends a POST request to the backend with user details.
     */
    const handleRegister = async (e) => {
        e.preventDefault(); // Prevents default form submission behavior
        try {
            const response = await axios.post(
                '/api/auth/register',
                { name, email, password, warehouseId: selectedWarehouse, role }, // User data payload
                { 
                    headers: {
                        'Content-Type': 'application/json' // Ensures proper content type
                    },
                    withCredentials: true // Sends cookies if required
                }
            );
            alert('Registration successful'); // Notifies the user of success
        } catch (error) {
            console.error('Registration failed', error); // Logs the error
            alert('Registration failed'); // Notifies the user of failure
        }
    };

    return (
        // Registration form
        <form onSubmit={handleRegister} className="register-container">
            <h1>Register</h1>
            
            {/* Name input field */}
            <div className="form-group">
                <label htmlFor="name">Name</label>
                <input 
                    type="text" 
                    value={name} 
                    onChange={(e) => setName(e.target.value)} 
                    placeholder="Enter your name" 
                    required 
                />
            </div>
            
            {/* Email input field */}
            <div className="form-group">
                <label htmlFor="email">Email</label>
                <input 
                    type="email" 
                    value={email} 
                    onChange={(e) => setEmail(e.target.value)} 
                    placeholder="Enter your email" 
                    required 
                />
            </div>
            
            {/* Password input field */}
            <div className="form-group">
                <label htmlFor="password">Password</label>
                <input 
                    type="password" 
                    value={password} 
                    onChange={(e) => setPassword(e.target.value)} 
                    placeholder="Enter your password" 
                    required 
                />
            </div>

            {/* Role selection dropdown */}
            <div className="form-group">
                <label htmlFor="role">Select Role</label>
                <select 
                    value={role} 
                    onChange={(e) => setRole(e.target.value)} 
                    required
                >
                    <option value="Manager">Manager</option>
                    <option value="Admin">Admin</option>
                </select>
            </div>
            
            {/* Warehouse selection dropdown */}
            <div className="form-group">
                <label htmlFor="warehouse">Select Warehouse</label>
                <select 
                    value={selectedWarehouse} 
                    onChange={(e) => setSelectedWarehouse(e.target.value)} 
                    required
                >
                    <option value="">Select Warehouse</option>
                    {/* Map over warehouses and create an option for each */}
                    {warehouses.map((warehouse) => (
                        <option key={warehouse.warehouseId} value={warehouse.warehouseId}>
                            {warehouse.location}
                        </option>
                    ))}
                </select>
            </div>
            
            {/* Submit button */}
            <button type="submit" className="submit-btn">Register</button>
        </form>
    );
}

export default Register;
