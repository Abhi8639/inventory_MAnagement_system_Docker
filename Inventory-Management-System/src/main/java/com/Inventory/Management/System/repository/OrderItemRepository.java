package com.Inventory.Management.System.repository;

import com.Inventory.Management.System.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
