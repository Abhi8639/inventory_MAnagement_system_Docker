package com.Inventory.Management.System.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Inventory.Management.System.model.Product;
import com.Inventory.Management.System.model.Stock;
import com.Inventory.Management.System.model.Warehouse;
import com.Inventory.Management.System.repository.StockRepository;

import jakarta.transaction.Transactional;

/**
 * StockService handles business logic for managing stock levels in warehouses.
 * It provides methods to retrieve, update, and add stock entries.
 */
@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    /**
     * Retrieves all stock entries for a specific warehouse.
     *
     * @param warehouseId The ID of the warehouse.
     * @return A list of stock entries for the given warehouse.
     */
    public List<Stock> getProductsByWarehouse(String warehouseId) {
        return stockRepository.findByWarehouseWarehouseId(warehouseId);
    }

    /**
     * Retrieves a specific stock entry for a product in a warehouse.
     *
     * @param productId   The ID of the product.
     * @param warehouseId The ID of the warehouse.
     * @return The stock entry if found, or null if no matching entry exists.
     */
    public Stock getStock(String productId, String warehouseId) {
        List<Stock> stockList = stockRepository.findByProductProductIdAndWarehouseWarehouseId(productId, warehouseId);
        return stockList.isEmpty() ? null : stockList.get(0);
    }

    /**
     * Updates the quantity of a specific stock entry for a product in a warehouse.
     *
     * @param productId   The ID of the product.
     * @param warehouseId The ID of the warehouse.
     * @param newQuantity The new quantity to set.
     * @return The updated stock entry, or null if no matching entry exists.
     */
    @Transactional
    public Stock updateStockQuantity(String productId, String warehouseId, int newQuantity) {
        Stock stock = getStock(productId, warehouseId);
        if (stock != null) {
            stock.setQuantity(newQuantity);
            return stockRepository.save(stock);
        }
        return null;
    }

    /**
     * Adds a new stock entry or updates an existing one by adding the specified quantity.
     *
     * @param productId   The ID of the product.
     * @param warehouseId The ID of the warehouse.
     * @param quantity    The quantity to add to the stock (can be negative to reduce).
     * @return The added or updated stock entry.
     */
    @Transactional
    public Stock addOrUpdateStock(String productId, String warehouseId, int quantity) {
        Stock stock = getStock(productId, warehouseId);

        if (stock != null) {
            // Updates existing stock entry.
            stock.setQuantity(stock.getQuantity() + quantity);
        } else {
            // Creates a new stock entry.
            stock = new Stock();

            Product product = new Product();
            product.setProductId(productId);
            stock.setProduct(product);

            Warehouse warehouse = new Warehouse();
            warehouse.setWarehouseId(warehouseId);
            stock.setWarehouse(warehouse);

            stock.setQuantity(quantity);
        }
        return stockRepository.save(stock);
    }
}
