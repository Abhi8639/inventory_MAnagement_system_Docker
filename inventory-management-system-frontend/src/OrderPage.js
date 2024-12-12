import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './OrderPage.css';

/**
 * OrderPage component allows users to place an order by entering customer information 
 * and selecting products with quantities. It integrates with backend APIs for data.
 */
function OrderPage() {
  // State to manage customer information
  const [customerInfo, setCustomerInfo] = useState({
    email: '',
    mobileNo: '',
    address: '',
    zipcode: '',
  });

  // State to handle zip code suggestions for user input
  const [zipSuggestions, setZipSuggestions] = useState([]);

  // State to manage order items added by the user
  const [orderItems, setOrderItems] = useState([
    { productId: '', productName: '', price: 0, quantity: 1, error: '' }
  ]);

  // State to store the list of available products fetched from the backend
  const [products, setProducts] = useState([]);

  // Effect to fetch products from the backend on component mount
  useEffect(() => {
    fetchProducts();
  }, []);

  /**
   * Fetches the list of products from the backend.
   */
  const fetchProducts = () => {
    axios
      .get('/api/products')
      .then(response => {
        console.log('Products fetched:', response.data);
        setProducts(response.data);
      })
      .catch(error => console.error('Error fetching products:', error));
  };

  /**
   * Handles changes in customer information fields.
   * Fetches zip code suggestions when the user types a zip code.
   */
  const handleCustomerInfoChange = (e) => {
    const { name, value } = e.target;
    setCustomerInfo({ ...customerInfo, [name]: value });

    if (name === 'zipcode' && value.length >= 3) {
      fetchZipSuggestions(value);
    }
  };

  /**
   * Fetches zip code suggestions from the backend based on user input.
   */
  const fetchZipSuggestions = (input) => {
    axios
      .get(`/api/places?input=${input}`)
      .then(response => {
        const { predictions, status } = response.data;

        if (status !== 'OK') {
          console.error('Backend Places API Error:', response.data.error_message || status);
          return;
        }

        const suggestions = predictions.map(prediction => ({
          id: prediction.place_id,
          text: prediction.description,
        }));
        setZipSuggestions(suggestions);
      })
      .catch(error => {
        console.error('Error fetching zip suggestions from backend:', error.response?.data || error.message);
      });
  };

  /**
   * Handles selection of a zip code suggestion.
   */
  const handleZipSelect = (suggestion) => {
    const zipcodeMatch = suggestion.text.match(/\b\d{5}(-\d{4})?\b/); 

    if (zipcodeMatch) {
      const zipcode = zipcodeMatch[0]; // Extract valid zip code
      setCustomerInfo({ ...customerInfo, zipcode }); // Update the zip code field
    } else {
      console.error('No valid zipcode found in the selected suggestion');
    }

    setZipSuggestions([]); // Clear suggestions after selection
  };

  /**
   * Handles changes in order item fields such as product selection and quantity.
   */
  const handleOrderItemChange = (index, field, value) => {
    const updatedItems = [...orderItems];

    if (field === 'productId') {
      const selectedProduct = products.find(product => product.productId === value);
      if (selectedProduct) {
        updatedItems[index] = {
          ...updatedItems[index],
          productId: selectedProduct.productId,
          productName: selectedProduct.name,
          price: selectedProduct.price,
          error: '',
        };
      } else {
        updatedItems[index].productId = '';
        updatedItems[index].productName = '';
        updatedItems[index].price = 0;
      }
    } else if (field === 'quantity') {
      const selectedProduct = products.find(product => product.productId === updatedItems[index].productId);
      const quantity = parseInt(value, 10);

      if (selectedProduct && quantity > selectedProduct.overallQuantity) {
        updatedItems[index].error = `Only ${selectedProduct.overallQuantity} items in stock`;
      } else {
        updatedItems[index].error = '';
      }

      updatedItems[index] = {
        ...updatedItems[index],
        quantity: quantity,
      };
    }

    setOrderItems(updatedItems);
  };

  /**
   * Adds a new product row to the order items.
   */
  const addProductRow = () => {
    setOrderItems([...orderItems, { productId: '', productName: '', price: 0, quantity: 1, error: '' }]);
  };

  /**
   * Removes a product row from the order items.
   */
  const removeProductRow = (index) => {
    const updatedItems = orderItems.filter((_, i) => i !== index);
    setOrderItems(updatedItems);
  };

  /**
   * Calculates the total price for an individual order item.
   */
  const calculateTotalPrice = (item) => {
    return (item.price * item.quantity).toFixed(2);
  };

  /**
   * Handles form submission to place an order.
   */
  const handleSubmit = (e) => {
    e.preventDefault();

    // Validates order items for errors or missing data
    const invalidItems = orderItems.filter(item => !item.productId || item.error);
    if (invalidItems.length > 0) {
      alert('Please resolve errors in order items.');
      return;
    }

    const orderData = { ...customerInfo, orderItems };
    console.log('Order Data:', orderData);

    // Submit the order to the backend
    axios
      .post('/api/orders', orderData)
      .then(response => {
        console.log('Order placed successfully:', response.data);
        setCustomerInfo({ email: '', mobileNo: '', address: '', zipcode: '' }); // Reset form
        setOrderItems([{ productId: '', productName: '', price: 0, quantity: 1, error: '' }]); // Reset items
      })
      .catch(error => console.error('Error placing order:', error));
  };

  return (
    <div className="order-page-container">
      <h1 className="order-page-title">Place Your Order</h1>
      <form onSubmit={handleSubmit} className="order-page-form">
        <div>
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={customerInfo.email}
            onChange={handleCustomerInfoChange}
            required
            className="order-page-input"
          />
        </div>
        <div>
          <label>Mobile No:</label>
          <input
            type="text"
            name="mobileNo"
            value={customerInfo.mobileNo}
            onChange={handleCustomerInfoChange}
            required
            className="order-page-input"
          />
        </div>
        <div>
          <label>Address:</label>
          <input
            type="text"
            name="address"
            value={customerInfo.address}
            onChange={handleCustomerInfoChange}
            required
            className="order-page-input"
          />
        </div>
        <div>
          <label>Zipcode:</label>
          <input
            type="text"
            name="zipcode"
            value={customerInfo.zipcode}
            onChange={handleCustomerInfoChange}
            required
            className="order-page-input"
          />
          {zipSuggestions.length > 0 && (
            <ul className="zip-suggestions">
              {zipSuggestions.map(suggestion => (
                <li
                  key={suggestion.id}
                  onClick={() => handleZipSelect(suggestion)}
                  className="zip-suggestion-item"
                >
                  {suggestion.text}
                </li>
              ))}
            </ul>
          )}
        </div>

        <h2>Order Items</h2>
        {orderItems.map((item, index) => (
          <div key={index} className="order-items-section">
            <select
              value={item.productId}
              onChange={(e) => handleOrderItemChange(index, 'productId', e.target.value)}
              required
              className="order-page-input"
            >
              <option value="">Select Product</option>
              {products.map(product => (
                <option key={product.productId} value={product.productId}>
                  {product.name}
                </option>
              ))}
            </select>

            <input
              type="number"
              value={item.quantity}
              onChange={(e) => handleOrderItemChange(index, 'quantity', e.target.value)}
              min="1"
              required
              className="order-page-input"
            />

            <div className="price-display">
              Price: £{calculateTotalPrice(item)}
            </div>

            {item.error && (
              <div className="error-message">
                {item.error}
              </div>
            )}

            {index > 0 && (
              <button
                type="button"
                onClick={() => removeProductRow(index)}
                className="remove-button"
              >
                Remove
              </button>
            )}
          </div>
        ))}

        <button
          type="button"
          onClick={addProductRow}
          className="add-product-button"
        >
          Add Another Product
        </button>

        <div>
          <button type="submit" className="order-page-button">
            Submit Order
          </button>
        </div>
      </form>
    </div>
  );
}

export default OrderPage;
