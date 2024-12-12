import React, { useEffect, useState } from "react";
import axios from "axios";
import './OrdersPage.css';

/**
 * OrdersPage component displays a list of orders and their details,
 * including customer information and ordered items.
 */
const OrdersPage = () => {
    // State to hold orders fetched from the backend
    const [orders, setOrders] = useState([]);
    
    // State to hold product details fetched from the backend
    const [products, setProducts] = useState([]);
    
    // State to manage loading status
    const [loading, setLoading] = useState(true);
    
    // State to manage error messages
    const [error, setError] = useState(null);

    /**
     * useEffect to fetch orders and products when the component mounts.
     * Uses async functions to fetch data from the backend APIs.
     */
    useEffect(() => {
        const fetchOrders = async () => {
            try {
                const ordersResponse = await axios.get("/api/orders");
                // Ensures the response is an array before setting it to state
                setOrders(Array.isArray(ordersResponse.data) ? ordersResponse.data : []);
            } catch (err) {
                setError("Error fetching orders"); // Sets error state if fetching orders fails
            }
        };

        const fetchProducts = async () => {
            try {
                const productsResponse = await axios.get("/api/products");
                setProducts(productsResponse.data); // Sets product data to state
            } catch (err) {
                console.error("Error fetching products:", err); // Logs error to the console
            }
        };

        /**
         * Fetches both orders and products in parallel.
         * Updates loading state once all data is fetched.
         */
        const fetchData = async () => {
            await Promise.all([fetchOrders(), fetchProducts()]); // Fetches orders and products concurrently
            setLoading(false); // Sets loading to false once data is fetched
        };

        fetchData(); // Triggers data fetching
    }, []);

    /**
     * Retrieves the product name based on the productId.
     *
     * @param {string} productId - The ID of the product.
     * @returns {string} The product name or "Unknown Product" if not found.
     */
    const getProductName = (productId) => {
        const product = products.find((p) => p.productId === productId);
        return product ? product.name : "Unknown Product"; // Returns product name or fallback text
    };

    // Displays loading message if data is still being fetched
    if (loading) return <p>Loading...</p>;

    // Displays error message if an error occurred during fetching
    if (error) return <p>{error}</p>;

    // Renders the list of orders
    return (
        <div className="orders-page">
            <h1>Order Details</h1>
            <div className="order-grid">
                {orders.map((order) => (
                    <div key={order.orderId} className="order-card">
                        <h2>Order ID: {order.orderId}</h2>
                        <p>Email: {order.email}</p>
                        <p>Mobile No: {order.mobileNo}</p>
                        <p>Address: {order.address}</p>
                        <p>Zipcode: {order.zipcode}</p>

                        <h3>Ordered Items:</h3>
                        <ul>
                            {order.orderItems.map((item) => (
                                <li key={item.orderItemId}>
                                    Product Name: {getProductName(item.productId)}, Quantity: {item.quantity}
                                </li>
                            ))}
                        </ul>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default OrdersPage;
