import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './OrderStockPage.css';
import { useNavigate } from 'react-router-dom';

/**
 * OrderStockPage component allows warehouse managers to add or update stock quantities
 * for products in their warehouse.
 */
function OrderStockPage() {
    // State for managing product list
    const [products, setProducts] = useState([]);
    // State for tracking the selected product
    const [selectedProductId, setSelectedProductId] = useState('');
    // State for tracking the input quantity
    const [newQuantity, setNewQuantity] = useState('');
    // State for displaying success messages
    const [successMessage, setSuccessMessage] = useState('');
    // State for displaying error messages
    const [errorMessage, setErrorMessage] = useState('');
    
    // React Router's useNavigate hook for programmatic navigation
    const navigate = useNavigate();
    // Retrieves the warehouse ID from localStorage
    const warehouseId = localStorage.getItem('warehouseId'); 
  
    /**
     * useEffect hook to fetch product data on component mount.
     * If warehouseId is not available, navigates the user to the login page.
     */
    useEffect(() => {
        if (warehouseId) {
            fetchProducts();
        } else {
            console.error("Warehouse ID not found in localStorage.");
            setErrorMessage("Warehouse ID not available. Please log in again.");
            navigate('/login'); // Redirects to login page
        }
    }, [warehouseId, navigate]);
  
    /**
     * Fetches the list of products from the server.
     * Updates the products state or sets an error message if the request fails.
     */
    const fetchProducts = async () => {
        try {
            const response = await axios.get('/api/products');
            if (response.data && Array.isArray(response.data)) {
                setProducts(response.data);
                setErrorMessage(''); // Clears any previous errors
            } else {
                console.error("Unexpected response format:", response);
                setErrorMessage("Failed to retrieve products. Please try again later.");
            }
        } catch (error) {
            console.error("Error fetching products:", error);
            setErrorMessage("Error fetching products. Please check the server connection.");
        }
    };
  
    /**
     * Sends a request to the backend to add or update stock quantity for the selected product.
     */
    const addOrUpdateStockQuantity = async () => {
        try {
            const response = await axios.post(`/api/stock/addOrUpdate`, {
                productId: selectedProductId,
                warehouseId: warehouseId,
                quantity: newQuantity,
            }); 
            if (response.status === 200) {
                setSuccessMessage('Stock added/updated successfully!'); // Shows success message
                setNewQuantity(''); // Resets quantity input
                setSelectedProductId(''); // Resets product selection
                fetchProducts(); // Refreshes product data
            }
        } catch (error) {
            console.error("Error adding/updating stock:", error);
            setErrorMessage("Failed to add/update stock. Please try again."); // Shows error message
        }
    };
  
    // Renders the component
    return (
        <div className="warehouse-stock">
            <h2>Order Stock</h2>
  
            {/* Displays error message if any */}
            {errorMessage && <p className="error-message">{errorMessage}</p>}
  
            {/* Displays product selection and quantity input if products are available */}
            {products.length > 0 && (
                <div className="update-section">
                    <label>Product Name:</label>
                    <select
                        value={selectedProductId}
                        onChange={(e) => setSelectedProductId(e.target.value)}
                    >
                        <option value="">Select Product</option>
                        {products.map((product) => (
                            <option key={product.productId} value={product.productId}>
                                {product.name}
                            </option>
                        ))}
                    </select>
  
                    <label>Quantity:</label>
                    <input
                        type="number"
                        value={newQuantity}
                        onChange={(e) => setNewQuantity(e.target.value)}
                        placeholder="Enter new quantity"
                    />
                    <button onClick={addOrUpdateStockQuantity}>Order Stock</button>
                </div>
            )}
  
            {/* Displays success message if any */}
            {successMessage && <p className="success-message">{successMessage}</p>}
        </div>
    );
}

export default OrderStockPage;
