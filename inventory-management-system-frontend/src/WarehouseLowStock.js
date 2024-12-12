import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './WarehouseStockPage.css';

/**
 * WarehouseLowStock component displays a list of products with low stock
 * in the logged-in user's warehouse.
 */
function WarehouseLowStock() {
  // State to hold low stock products
  const [lowStockProducts, setLowStockProducts] = useState([]);
  
  // State to manage error messages
  const [errorMessage, setErrorMessage] = useState('');
  
  // React Router's navigation hook
  const navigate = useNavigate();
  
  // Retrieves warehouse ID from localStorage
  const warehouseId = localStorage.getItem('warehouseId'); 

  /**
   * useEffect hook to fetch low stock products when the component mounts.
   * If no warehouse ID is found, navigates the user to the login page.
   */
  useEffect(() => {
    if (warehouseId) {
      fetchLowStockProducts(); // Fetches low stock products
    } else {
      console.error("Warehouse ID not found in localStorage.");
      setErrorMessage("Warehouse ID not available. Please log in again.");
      navigate('/login'); // Redirects to login page
    }
  }, [warehouseId, navigate]);

  /**
   * Fetches products with low stock for the current warehouse.
   * Filters products with quantities less than 10.
   */
  const fetchLowStockProducts = async () => {
    try {
      console.log(`Fetching products with low stock for warehouse ID: ${warehouseId}`);
      const response = await axios.get(`/api/stock/warehouse/${warehouseId}`);
      
      if (response.data && Array.isArray(response.data)) {
        const lowStock = response.data.filter((product) => product.quantity < 10); // Filters low stock products
        setLowStockProducts(lowStock); // Updates state with low stock products
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


  return (
    <div className="warehouse-stock">
      <h2>Low Stock Products</h2>

      {errorMessage && <p className="error-message">{errorMessage}</p>}

      {lowStockProducts.length > 0 ? (
        <table>
          <thead>
            <tr>
              <th>Product Name</th>
              <th>Available Stock</th>
            </tr>
          </thead>
          <tbody>
            {lowStockProducts.map((product) => (
              <tr key={product.stockId}>
                <td>{product.product.name}</td>
                <td>{product.quantity}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>All products have sufficient stock in this warehouse.</p>
      )}
    </div>
  );
}

export default WarehouseLowStock;
