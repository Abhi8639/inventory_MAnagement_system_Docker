import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './ProductManagement.css';
import './Navbar.css';
import './App.css'

/**
 * ProductManagement component allows users to manage a list of products.
 * Includes functionality to add, edit, and delete products.
 */
function ProductManagement() {
  // State for storing the list of products
  const [products, setProducts] = useState([]);
  
  // State for managing the new product form
  const [newProduct, setNewProduct] = useState({ name: '', price: 0, overallQuantity: 0 });
  
  // State for managing the product being edited
  const [editProduct, setEditProduct] = useState(null);

  // Fetches products when the component mounts
  useEffect(() => {
    fetchProducts();
  }, []);

  /**
   * Fetches the list of products from the backend API
   * and updates the `products` state.
   */
  const fetchProducts = () => {
    axios.get('/api/products')
      .then(response => {
        console.log('Products fetched:', response.data);
        const productsData = Array.isArray(response.data) ? response.data : [];
        setProducts(productsData);
      })
      .catch(error => console.error('Error fetching products:', error));
  };

  /**
   * Handles input changes for the new product form.
   */
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewProduct({ ...newProduct, [name]: value });
  };

  /**
   * Handles input changes for the edit product form.
   */
  const handleEditChange = (e) => {
    const { name, value } = e.target;
    setEditProduct({ ...editProduct, [name]: value });
  };

  /**
   * Adds a new product by sending a POST request to the backend API.
   */
  const addProduct = (e) => {
    e.preventDefault();
    axios.post('/api/products/add', newProduct)
      .then(() => {
        fetchProducts(); // Refreshes the product list
        setNewProduct({ name: '', price: 0, overallQuantity: 0 }); // Resets the form
      })
      .catch(error => console.error('Error adding product:', error));
  };

  /**
   * Updates an existing product's details by sending a PUT request to the backend API.
   */
  const updateProductDetails = (e) => {
    e.preventDefault();
    axios.put(`/api/products/update/${editProduct.productId}`, editProduct)
      .then(() => {
        fetchProducts(); // Refreshes the product list
        setEditProduct(null); // Clears the edit form
      })
      .catch(error => console.error('Error updating product:', error));
  };

  /**
   * Deletes a product by sending a DELETE request to the backend API.
   */
  const deleteProduct = (productId) => {
    axios.delete(`/api/products/delete/${productId}`)
      .then(() => fetchProducts()) // Refresh the product list
      .catch(error => console.error('Error deleting product:', error));
  };

  /**
   * Selects a product for editing by populating the `editProduct` state.
   */
  const selectProductForEditing = (product) => {
    setEditProduct({ ...product });
  };

  return (
    <>
      {/* Spacer to prevent overlap with the fixed navbar */}
      <div className="navbar-spacer"></div>
      
      <div className="product-management">
        <h1>Product Management</h1>

        {/* Form for adding a new product */}
        <form onSubmit={addProduct}>
          <h2>Add New Product</h2>
          <div className="form-group">
            <label htmlFor="name">Product Name</label>
            <input 
              type="text" 
              name="name" 
              id="name"
              value={newProduct.name} 
              onChange={handleInputChange} 
              placeholder="Product Name" 
              required 
            />
          </div>
          <div className="form-group">
            <label htmlFor="price">Price</label>
            <input 
              type="number" 
              name="price" 
              id="price"
              value={newProduct.price} 
              onChange={handleInputChange} 
              placeholder="Price" 
              required 
            />
          </div>
          <div className="form-group">
            <label htmlFor="overallQuantity">Quantity</label>
            <input 
              type="number" 
              name="overallQuantity" 
              id="overallQuantity"
              value={newProduct.overallQuantity} 
              onChange={handleInputChange} 
              placeholder="Quantity" 
              required 
            />
          </div>
          <button type="submit" className="submit-btn">Add Product</button>
        </form>

        {/* Form for editing an existing product */}
        {editProduct && (
          <form onSubmit={updateProductDetails}>
            <h2>Edit Product</h2>
            <div className="form-group">
              <label htmlFor="editName">Product Name</label>
              <input 
                type="text" 
                name="name" 
                id="editName"
                value={editProduct.name} 
                onChange={handleEditChange} 
                placeholder="Product Name" 
                required 
              />
            </div>
            <div className="form-group">
              <label htmlFor="editPrice">Price</label>
              <input 
                type="number" 
                name="price" 
                id="editPrice"
                value={editProduct.price} 
                onChange={handleEditChange} 
                placeholder="Price" 
                required 
              />
            </div>
            <div className="form-group">
              <label htmlFor="editQuantity">Quantity</label>
              <input 
                type="number" 
                name="overallQuantity" 
                id="editQuantity"
                value={editProduct.overallQuantity} 
                onChange={handleEditChange} 
                placeholder="Quantity" 
                required 
              />
            </div>
            <button type="submit" className="submit-btn">Update Product</button>
          </form>
        )}

        {/* Table to display the list of products */}
        <h2>Product List</h2>
        <table className="product-table">
          <thead>
            <tr>
              <th>Product Name</th>
              <th>Price</th>
              <th>Quantity</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {Array.isArray(products) && products.length > 0 ? (
              products.map(product => (
                <tr key={product.productId}>
                  <td>{product.name}</td>
                  <td>£{product.price}</td>
                  <td>{product.overallQuantity}</td>
                  <td>
                    <div className="button-container">
                      <button className="edit-btn" onClick={() => selectProductForEditing(product)}>Edit</button>
                      <button className="delete-btn" onClick={() => deleteProduct(product.productId)}>Delete</button>
                    </div>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="4">No products available</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </>
  );
}

export default ProductManagement;
