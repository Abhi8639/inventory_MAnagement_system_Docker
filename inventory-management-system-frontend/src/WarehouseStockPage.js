import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './WarehouseStockPage.css';

/**
 * WarehouseStockPage component manages the stock levels of products in a specific warehouse.
 */
function WarehouseStockPage() {
  // State variables for managing products, form inputs, and messages
  const [products, setProducts] = useState([]);
  const [selectedProductId, setSelectedProductId] = useState('');
  const [newQuantity, setNewQuantity] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  // Uses React Router's navigate function for redirection
  const navigate = useNavigate();
  
  // Fetches the warehouse ID from local storage
  const warehouseId = localStorage.getItem('warehouseId');

  /**
   * Effect hook to fetch products when the component is mounted.
   * Redirects to the login page if the warehouse ID is missing.
   */
  useEffect(() => {
    if (warehouseId) {
      fetchProducts(); // Fetches products for the warehouse
    } else {
      console.error("Warehouse ID not found in localStorage.");
      setErrorMessage("Warehouse ID not available. Please log in again.");
      navigate('/login'); // Redirects to login page
    }
  }, [warehouseId, navigate]);

  /**
   * Fetches products for the current warehouse from the backend.
   */
  const fetchProducts = async () => {
    try {
      console.log(`Fetching products for warehouse ID: ${warehouseId}`);
      const response = await axios.get(`/api/stock/warehouse/${warehouseId}`);
      
      if (response.data && Array.isArray(response.data)) {
        setProducts(response.data); // Updates product list
        setErrorMessage(''); // Clears any previous error messages
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
   * Updates the stock quantity for a selected product.
   */
  const updateStockQuantity = async () => {
    try {
      const response = await axios.put(`/api/stock/update`, {
        productId: selectedProductId,
        warehouseId: warehouseId,
        quantity: newQuantity,
      }); 
      if (response.status === 200) {
        setSuccessMessage('Stock updated successfully!'); // Shows success message
        setNewQuantity(''); // Clears the input field
        setSelectedProductId(''); // Resets the dropdown
        fetchProducts(); // Refreshes the product list
      }
    } catch (error) {
      console.error("Error updating stock:", error);
      setErrorMessage("Failed to update stock. Please try again."); // Shows error message
    }
  };


  return (
    <div className="warehouse-stock">
      <h2>Warehouse Stock Management</h2>

      {errorMessage && <p className="error-message">{errorMessage}</p>}

      <h3>Products in Warehouse</h3>
      {products.length > 0 ? (
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Current Quantity</th>
            </tr>
          </thead>
          <tbody>
            {products.map((stock) => (
              <tr key={stock.stockId}>
                <td>{stock.product.name}</td>  
                <td>{stock.quantity}</td>  
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>No products found for this warehouse.</p>
      )}

      {products.length > 0 && (
        <div className="update-section">
          <h4>Update Product Quantity</h4>
          <label>Product Name:</label>
          <select
            value={selectedProductId}
            onChange={(e) => setSelectedProductId(e.target.value)}
          >
            <option value="">Select Product</option>
            {products.map((stock) => (
              <option key={stock.product.productId} value={stock.product.productId}>
                {stock.product.name}
              </option>
            ))}
          </select>

          <label>New Quantity:</label>
          <input
            type="number"
            value={newQuantity}
            onChange={(e) => setNewQuantity(e.target.value)}
            placeholder="Enter new quantity"
          />
          <button onClick={updateStockQuantity}>Update Quantity</button>
        </div>
      )}

      {successMessage && <p className="success-message">{successMessage}</p>}
    </div>
  );
}

export default WarehouseStockPage;
