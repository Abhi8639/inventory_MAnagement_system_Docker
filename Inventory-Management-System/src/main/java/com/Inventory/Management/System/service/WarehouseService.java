package com.Inventory.Management.System.service;

import com.Inventory.Management.System.model.Warehouse;
import com.Inventory.Management.System.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * WarehouseService handles business logic related to managing warehouses,
 * including adding, retrieving, updating, and deleting warehouses.
 */
@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    /**
     * Adds a new warehouse to the system.
     *
     * @param warehouse The warehouse entity to be added.
     * @return The saved warehouse entity.
     */
    public Warehouse addWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    /**
     * Retrieves all warehouses from the database.
     *
     * @return A list of all warehouses.
     */
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    /**
     * Updates the details of an existing warehouse.
     *
     * @param warehouseId     The ID of the warehouse to update.
     * @param warehouseDetails The new details for the warehouse.
     * @return The updated warehouse entity.
     * @throws RuntimeException If the warehouse with the given ID is not found.
     */
    public Warehouse updateWarehouse(String warehouseId, Warehouse warehouseDetails) {
        // Retrieves the warehouse by ID or throw an exception if not found.
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
            .orElseThrow(() -> new RuntimeException("Warehouse with ID: " + warehouseId + " not found"));

        // Updates warehouse details.
        warehouse.setLocation(warehouseDetails.getLocation());
        warehouse.setCapacity(warehouseDetails.getCapacity());
        warehouse.setZipcode(warehouseDetails.getZipcode());

        // Saves and return the updated warehouse.
        return warehouseRepository.save(warehouse);
    }

    /**
     * Deletes a warehouse from the system.
     *
     * @param warehouseId The ID of the warehouse to delete.
     * @throws RuntimeException If the warehouse with the given ID is not found.
     */
    public void deleteWarehouse(String warehouseId) {
        // Retrieves the warehouse by ID or throw an exception if not found.
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
            .orElseThrow(() -> new RuntimeException("Warehouse with ID: " + warehouseId + " not found"));

        // Deletes the warehouse.
        warehouseRepository.delete(warehouse);
    }
}
