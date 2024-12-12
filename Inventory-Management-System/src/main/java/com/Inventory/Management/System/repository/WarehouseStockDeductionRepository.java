package com.Inventory.Management.System.repository;

import com.Inventory.Management.System.model.WarehouseStockDeduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseStockDeductionRepository extends JpaRepository<WarehouseStockDeduction, String> {

    @Query("SELECT wsd FROM WarehouseStockDeduction wsd JOIN FETCH wsd.product p WHERE wsd.warehouseId = :warehouseId")
    List<WarehouseStockDeduction> findByWarehouseIdWithProductName(@Param("warehouseId") String warehouseId);
}
