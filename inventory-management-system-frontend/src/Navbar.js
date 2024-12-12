import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './Navbar.css'; // Importing CSS for styling the Navbar

/**
 * The Navbar component provides a navigation bar for the application.
 * It dynamically adjusts the visible links based on the user's login status and role.
 */
function Navbar() {
  // React Router's useNavigate hook for programmatic navigation
  const navigate = useNavigate();

  // Determines if the user is logged in by checking localStorage
  const isLoggedIn = localStorage.getItem('login') === 'true';
  
  // Retrieves the user's role from localStorage for role-based navigation
  const userRole = localStorage.getItem('role'); 
  
  /**
   * Handles the logout functionality by clearing the login state
   * and redirecting the user to the login page.
   */
  const handleLogout = () => {
    localStorage.removeItem('login'); // Clears login status
    localStorage.removeItem('role'); // Clears user role
    navigate('/login'); // Redirects to login page
  };

  // Renders the Navbar
  return (
    <nav className="navbar">
      {/* Logo section, linking to the home page */}
      <div className="logo">
        <Link to="/">IMS</Link> {/* "IMS" represents Inventory Management System */}
      </div>
      <div className="nav-links">
        {/* Conditional rendering based on login status */}
        {isLoggedIn ? (
          <>
            {/* Links available to Admin users */}
            {userRole === 'Admin' && (
              <>
                <Link to="/allorders">All Orders</Link>
                <Link to="/products">Product Management</Link>
                <Link to="/warehouses">Warehouse Management</Link>
                <Link to="/register">Register</Link>
              </>
            )}
            {/* Links available to Manager users */}
            {userRole === 'Manager' && (
              <>
                <Link to="/warehouse-stock">Warehouse Stock Management</Link>
                <Link to="/stockorder">Order Stock</Link>
                <Link to="/stockdeductions">Orders</Link>
                <Link to="/warehouse-low-stock">Low Stock Products</Link>
              </>
            )}
            {/* Common logout link for all logged-in users */}
            <Link to="/" onClick={handleLogout}>Logout</Link>
          </>
        ) : (
          <>
            {/* Links available to non-logged-in users */}
            <Link to="/login">Login</Link>
            <Link to="/orders">Order Management</Link>
          </>
        )}
      </div>
    </nav>
  );
}

export default Navbar;
