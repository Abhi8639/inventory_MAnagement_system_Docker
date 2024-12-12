package com.Inventory.Management.System.controller;

import com.Inventory.Management.System.model.Warehouse;
import com.Inventory.Management.System.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * WarehouseController handles RESTful endpoints for managing warehouses.
 * It provides functionality to add, retrieve, update, and delete warehouses in the system.
 */
@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    // Injecting the WarehouseService to handle warehouse-related business logic.
    @Autowired
    private WarehouseService warehouseService;

    /**
     * It is the Endpoint to add a new warehouse.
     * Accepts a Warehouse object in the request body and creates a new warehouse.
     *
     * @param warehouse The Warehouse object containing details of the new warehouse.
     * @return The created Warehouse object.
     */
    @PostMapping("/add")
    public Warehouse addWarehouse(@RequestBody Warehouse warehouse) {
        return warehouseService.addWarehouse(warehouse);
    }

    /**
     * It is the Endpoint to retrieve all warehouses.
     * Returns a list of all warehouses in the system.
     *
     * @return A list of Warehouse objects.
     */
    @GetMapping
    public List<Warehouse> getAllWarehouses() {
        return warehouseService.getAllWarehouses();
    }

    /**
     * It is the Endpoint to update a specific warehouse.
     * Accepts a warehouse ID as a path variable and updated details in the request body.
     *
     * @param warehouseId     The ID of the warehouse to be updated.
     * @param warehouseDetails The Warehouse object containing updated details.
     * @return The updated Warehouse object.
     */
    @PutMapping("/update/{warehouseId}")
    public Warehouse updateWarehouse(@PathVariable("warehouseId") String warehouseId, @RequestBody Warehouse warehouseDetails) {
        return warehouseService.updateWarehouse(warehouseId, warehouseDetails);
    }

    /**
     * It is the Endpoint to delete a specific warehouse.
     * Accepts a warehouse ID as a path variable and deletes the warehouse.
     *
     * @param warehouseId The ID of the warehouse to be deleted.
     * @return A ResponseEntity with no content to indicate successful deletion.
     */
    @DeleteMapping("/delete/{warehouseId}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable("warehouseId") String warehouseId) {
        warehouseService.deleteWarehouse(warehouseId);
        return ResponseEntity.ok().build();
    }
}
