import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './WarehouseManagement.css';

/**
 * WarehouseManagement component allows users to manage warehouses by adding, updating, 
 * and deleting warehouse information. It displays a list of existing warehouses 
 * and provides a form for input.
 */
function WarehouseManagement() {
  // State for managing warehouse data
  const [warehouses, setWarehouses] = useState([]); // List of warehouses
  const [newWarehouse, setNewWarehouse] = useState({
    location: '',
    capacity: '',
    zipcode: '', 
  }); // State for new or edited warehouse details
  
  const [editMode, setEditMode] = useState(false); // Determines if the form is in edit mode
  const [editWarehouseId, setEditWarehouseId] = useState(null); // ID of the warehouse being edited

  // Fetches warehouses on component mount
  useEffect(() => {
    fetchWarehouses();
  }, []);

  /**
   * Fetches the list of warehouses from the server.
   */
  const fetchWarehouses = async () => {
    try {
      const response = await axios.get('/api/warehouses');
      const data = Array.isArray(response.data) ? response.data : [];
      setWarehouses(data); // Updates state with warehouse data
    } catch (error) {
      console.error('Error fetching warehouses:', error);
      setWarehouses([]); // Clears the warehouse list on error
    }
  };

  /**
   * Handles changes in the input fields.
   * @param {Event} e - The input change event.
   */
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewWarehouse((prevWarehouse) => ({
      ...prevWarehouse,
      [name]: value,
    }));
  };

  /**
   * Adds a new warehouse or updates an existing one based on the edit mode.
   */
  const handleAddOrUpdateWarehouse = async () => {
    try {
      if (editMode) {
        // Updates existing warehouse
        await axios.put(`/api/warehouses/update/${editWarehouseId}`, newWarehouse);
      } else {
        // Adds a new warehouse
        await axios.post('/api/warehouses/add', newWarehouse);
      }
      fetchWarehouses(); // Refreshes the warehouse list
      setNewWarehouse({ location: '', capacity: '', zipcode: '' }); // Resets the form
      setEditMode(false); // Exits edit mode
      setEditWarehouseId(null); // Clears the ID of the edited warehouse
    } catch (error) {
      console.error('Error adding/updating warehouse:', error);
    }
  };

  /**
   * Deletes a warehouse based on the given ID.
   * @param {string} warehouseId - The ID of the warehouse to delete.
   */
  const handleDeleteWarehouse = async (warehouseId) => {
    try {
      await axios.delete(`/api/warehouses/delete/${warehouseId}`);
      fetchWarehouses(); // Refreshes the warehouse list
    } catch (error) {
      console.error('Error deleting warehouse:', error);
    }
  };

  /**
   * Prepares the form for editing an existing warehouse.
   * @param {Object} warehouse - The warehouse to edit.
   */
  const handleEditClick = (warehouse) => {
    setNewWarehouse({
      location: warehouse.location,
      capacity: warehouse.capacity,
      zipcode: warehouse.zipcode, 
    });
    setEditWarehouseId(warehouse.warehouseId); // Sets the ID of the warehouse being edited
    setEditMode(true); // Enables edit mode
  };
  return (
    <div className="warehouse-management">
      <h1>Warehouse Management</h1>

      <h2>{editMode ? 'Edit Warehouse' : 'Add a Warehouse'}</h2>
      <div className="form-group">
        <label htmlFor="location">Location</label>
        <input
          type="text"
          name="location"
          value={newWarehouse.location}
          placeholder="Location"
          onChange={handleInputChange}
          className="form-control"
        />
      </div>
      <div className="form-group">
        <label htmlFor="capacity">Capacity</label>
        <input
          type="number"
          name="capacity"
          value={newWarehouse.capacity}
          placeholder="Capacity"
          onChange={handleInputChange}
          className="form-control"
        />
      </div>
      <div className="form-group">
        <label htmlFor="zipcode">Zipcode</label>
        <input
          type="text"
          name="zipcode"
          value={newWarehouse.zipcode} 
          placeholder="Zipcode"
          onChange={handleInputChange}
          className="form-control"
        />
      </div>
      <button onClick={handleAddOrUpdateWarehouse} className="submit-btn">
        {editMode ? 'Update Warehouse' : 'Add Warehouse'}
      </button>

      <h2>Warehouses List</h2>
      <table className="warehouse-table">
        <thead>
          <tr>
            <th>Location</th>
            <th>Capacity</th>
            <th>Zipcode</th> 
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {Array.isArray(warehouses) && warehouses.length > 0 ? (
            warehouses.map((warehouse) => (
              <tr key={warehouse.warehouse_id}>
                <td>{warehouse.location}</td>
                <td>{warehouse.capacity}</td>
                <td>{warehouse.zipcode}</td> 
                <td className="actions">
                  <div className="button-container">
                    <button onClick={() => handleEditClick(warehouse)} className="edit-btn">
                      Edit
                    </button>
                    <button onClick={() => handleDeleteWarehouse(warehouse.warehouseId)} className="delete-btn">
                      Delete
                    </button>
                  </div>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="4">No warehouses available</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default WarehouseManagement;
