package com.Inventory.Management.System.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Inventory.Management.System.model.WarehouseStockDeduction;
import com.Inventory.Management.System.repository.WarehouseStockDeductionRepository;

import java.util.List;

/**
 * StockDeductionController handles RESTful endpoints for managing and retrieving
 * stock deduction details in the inventory system.
 */
@RestController
@RequestMapping("/api/stock-deductions")
public class StockDeductionController {

    // Injecting the WarehouseStockDeductionRepository to interact with the data layer.
    @Autowired
    private WarehouseStockDeductionRepository warehouseStockDeductionRepository;

    /**
     * It is the Endpoint to retrieve stock deductions for a specific warehouse.
     * Fetches a list of stock deduction records along with the associated product names.
     *
     * @param warehouseId The ID of the warehouse for which stock deductions are being fetched.
     * @return A list of WarehouseStockDeduction objects for the specified warehouse.
     */
    @GetMapping("/warehouse/{warehouseId}")
    public List<WarehouseStockDeduction> getDeductionsByWarehouse(@PathVariable String warehouseId) {
        // Calls the repository to fetch stock deductions for the given warehouse ID.
        return warehouseStockDeductionRepository.findByWarehouseIdWithProductName(warehouseId);
    }
}
