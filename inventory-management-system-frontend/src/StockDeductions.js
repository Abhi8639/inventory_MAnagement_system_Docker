import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './WarehouseStockPage.css';

/**
 * StockDeductions component displays the list of stock deductions for a specific warehouse.
 */
const StockDeductions = () => {
  // State for storing stock deductions data
  const [deductions, setDeductions] = useState([]);
  
  // State for handling error messages
  const [errorMessage, setErrorMessage] = useState('');
  
  // React Router's navigation hook
  const navigate = useNavigate();
  
  // Retrieves warehouse ID from localStorage
  const warehouseId = localStorage.getItem('warehouseId');

  /**
   * useEffect hook to fetch stock deductions when the component mounts.
   * If no warehouse ID is found, navigates the user to the login page.
   */
  useEffect(() => {
    if (warehouseId) {
      fetchDeductions(); // Fetches stock deductions
    } else {
      console.error("Warehouse ID not found in localStorage.");
      setErrorMessage("Warehouse ID not available. Please log in again.");
      navigate('/login'); // Redirects to login page
    }
  }, [warehouseId, navigate]);

  /**
   * Fetches stock deductions data from the backend.
   * Updates the `deductions` state or sets an error message if the request fails.
   */
  const fetchDeductions = async () => {
    try {
      const response = await axios.get(`/api/stock-deductions/warehouse/${warehouseId}`);
      setDeductions(response.data); // Updates state with fetched data
      setErrorMessage(''); // Clears any previous errors
    } catch (error) {
      console.error("Error fetching stock deductions:", error);
      setErrorMessage("Error fetching stock deductions. Please check the server connection.");
    }
  };

  // Renders the component
  return (
    <div className="warehouse-stock">
      <h2>Stock Deductions</h2>

      {errorMessage && <p className="error-message">{errorMessage}</p>}

      {deductions.length > 0 ? (
        <table>
          <thead>
            <tr>
              <th>Order ID</th>
              <th>Product Name</th>
              <th>Quantity Deducted</th>
              <th>Date</th>
            </tr>
          </thead>
          <tbody>
          {deductions.map((deduction) => (
  <tr key={deduction.deductionId}>
    <td>{deduction.orderId}</td>
    <td>{deduction.product ? deduction.product.name : "N/A"}</td> 
    <td>{deduction.quantityDeducted}</td>
    <td>{new Date(deduction.deductionTimestamp).toLocaleString()}</td>
  </tr>
))}

          </tbody>
        </table>
      ) : (
        <p>No stock deductions recorded for this warehouse.</p>
      )}
    </div>
  );
};

export default StockDeductions;
