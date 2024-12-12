package com.Inventory.Management.System.repository;

import com.Inventory.Management.System.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, String> {
    List<Stock> findByWarehouseWarehouseId(String warehouseId);

    List<Stock> findByProductProductIdAndWarehouseWarehouseId(String productId, String warehouseId);
}
