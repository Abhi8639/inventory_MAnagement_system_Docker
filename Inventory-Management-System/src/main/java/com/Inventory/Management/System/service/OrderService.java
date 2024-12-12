package com.Inventory.Management.System.service;

import com.Inventory.Management.System.model.*;
import com.Inventory.Management.System.repository.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OrderService provides functionality for managing orders, including order creation,
 * processing, and ensuring inventory updates in warehouses.
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private StockService stockService;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private WarehouseStockDeductionRepository warehouseStockDeductionRepository;

    /**
     * Creates a new order, saves it to the database, and processes it by allocating stock from warehouses.
     *
     * @param order The order to be created.
     * @return The saved order.
     * @throws IllegalArgumentException If the order or its items are null or empty.
     * @throws RuntimeException         If an error occurs during order saving or processing.
     */
    @Transactional
    public Order createOrder(Order order) {
        try {
            if (order == null || order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
                throw new IllegalArgumentException("Order or order items cannot be null or empty");
            }

            System.out.println("Saving Order: " + order);

            // Saves the order to the database.
            Order savedOrder = orderRepository.save(order);
            System.out.println("Order saved with ID: " + savedOrder.getOrderId());

            // Saves each order item to the database and associate it with the saved order.
            for (OrderItem item : order.getOrderItems()) {
                if (item.getProductId() == null) {
                    throw new IllegalArgumentException("Invalid product in order item.");
                }
                item.setOrder(savedOrder);
                System.out.println("Saving OrderItem with Product ID: " + item.getProductId());

                orderItemRepository.save(item);
            }

            // Processes the order to allocate stock from warehouses.
            processOrder(savedOrder);
            return savedOrder;

        } catch (Exception e) {
            e.printStackTrace(); 
            throw new RuntimeException("Error saving the order", e);
        }
    }

    /**
     * Retrieves all orders from the database.
     *
     * @return A list of all orders.
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Processes an order by allocating stock from warehouses based on proximity and availability.
     *
     * @param order The order to be processed.
     * @throws RuntimeException If there is insufficient stock to fulfill the order.
     */
    @Transactional
    public void processOrder(Order order) {
        String orderZipcode = order.getZipcode();

        // Retrieves all warehouses and sort them by proximity to the order's location.
        List<Warehouse> allWarehouses = warehouseRepository.findAll();
        List<Warehouse> sortedWarehouses = locationService.getWarehousesByProximity(orderZipcode, allWarehouses);

        for (OrderItem item : order.getOrderItems()) {
            int requiredQuantity = item.getQuantity();
            String productId = item.getProductId();

            for (Warehouse warehouse : sortedWarehouses) {
                if (requiredQuantity <= 0) break;

                // Fetches the stock of the product in the current warehouse.
                Stock stock = stockService.getStock(productId, warehouse.getWarehouseId());

                if (stock != null && stock.getQuantity() > 0) {
                    int allocatedQuantity = Math.min(stock.getQuantity(), requiredQuantity);

                    // Updates the stock quantity in the warehouse.
                    stockService.updateStockQuantity(productId, warehouse.getWarehouseId(), stock.getQuantity() - allocatedQuantity);

                    // Records the stock deduction for audit purposes.
                    WarehouseStockDeduction deduction = new WarehouseStockDeduction();
                    deduction.setOrderId(order.getOrderId());
                    deduction.setProductId(productId);
                    deduction.setWarehouseId(warehouse.getWarehouseId());
                    deduction.setQuantityDeducted(allocatedQuantity);
                    deduction.setDeductionTimestamp(LocalDateTime.now());

                    warehouseStockDeductionRepository.save(deduction);

                    requiredQuantity -= allocatedQuantity;
                }
            }

            // If the required quantity cannot be fulfilled, throw an exception.
            if (requiredQuantity > 0) {
                throw new RuntimeException("Insufficient stock to fulfill order for product ID: " + productId);
            }
        }
    }
}
