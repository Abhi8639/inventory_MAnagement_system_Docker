import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Login.css'; // Importing custom styles for the login page

/**
 * Login component allows users to authenticate by providing their email and password.
 * Based on the user's role, they are redirected to different pages after successful login.
 */
function Login() {
    // State hooks to manage email and password inputs
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    // React Router's useNavigate hook for programmatic navigation
    const navigate  = useNavigate();

    /**
     * Handles the login form submission.
     * Sends a POST request to the server with email and password.
     * Redirects the user based on their role upon successful login.
     *
     * @param {Event} e The form submission event.
     */
    const handleLogin = async (e) => {
        e.preventDefault(); // Prevent default form submission behavior
        try {
            // Sends a POST request to the login endpoint
            const response = await axios.post('/api/auth/login', 
                { email, password }, // Requests payload
                { 
                    headers: {
                        'Content-Type': 'application/json' // Sets the request content type
                    }
                });
            
            // Checks the login status from the response
            if (response.data.login === "true") {
                // Stores login-related information in localStorage for session management
                localStorage.setItem('login', 'true');
                localStorage.setItem('role', response.data.role); 
                localStorage.setItem('warehouseId', response.data.warehouseId);

                // Redirects the user based on their role
                if (response.data.role === 'Admin') {
                    navigate('/products');  // Admins are redirected to the Product Management page
                } else if (response.data.role === 'Manager') {
                    navigate('/warehouse-stock'); // Managers are redirected to the Warehouse Stock page
                } else {
                    navigate('/orders');  // Other roles are redirected to the Orders page
                }
            } else {
                alert('Login failed'); // Alerts the user if login fails
            }
        } catch (error) {
            console.error('Login failed', error); // Logs the error for debugging
            alert('Login failed'); // Notifies the user of a failure
        }
    };

    // Renders the login form
    return (
        <div className="login-container">
            <h1>Login</h1>
            <form onSubmit={handleLogin}>
                {/* Email input field */}
                <div className="form-group">
                    <label htmlFor="email">Email</label>
                    <input 
                        type="email" 
                        value={email} 
                        onChange={(e) => setEmail(e.target.value)} 
                        placeholder="Email" 
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
                        placeholder="Password" 
                        required 
                    />
                </div>

                {/* Submit button */}
                <button type="submit" className="submit-btn">Login</button>
            </form>
        </div>
    );
}

export default Login;
