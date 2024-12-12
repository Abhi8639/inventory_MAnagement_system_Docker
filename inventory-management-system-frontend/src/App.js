import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import OrderPage from './OrderPage'; 
import ProductManagement from './ProductManagement';
import WarehouseManagement from './WarehouseManagement'; 
import WarehouseStockPage from './WarehouseStockPage'; 
import Login from './Login';
import Register from './Register';
import Navbar from './Navbar'; 
import OrdersPage from './OrdersPage';
import WarehouseLowStock from './WarehouseLowStock';
import OrderStockPage from './OrderStockPage';
import StockDeductions from './StockDeductions';

/**
 * The `App` component serves as the main entry point for the application.
 * It sets up routing for different pages using React Router and includes a shared navigation bar.
 */
function App() {
  return (
    <Router>
      <div className="App">
        <Navbar />
        {/* Spacer for the fixed navbar */}
        <div className="content-wrapper">
          <Routes>
            <Route path="/orders" element={<OrderPage />} />
            <Route path="/products" element={<ProductManagement />} />
            <Route path="/warehouses" element={<WarehouseManagement />} />
            <Route path="/warehouse-stock" element={<WarehouseStockPage />} />
            <Route path="/warehouse-low-stock" element={<WarehouseLowStock />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/allorders" element={<OrdersPage />} />
            <Route path="/stockorder" element={<OrderStockPage />} />
            <Route path="/stockdeductions" element={<StockDeductions />} />
            <Route path="/" element={<OrderPage />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;


