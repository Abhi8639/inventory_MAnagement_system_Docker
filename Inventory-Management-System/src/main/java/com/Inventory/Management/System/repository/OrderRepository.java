package com.Inventory.Management.System.repository;

import com.Inventory.Management.System.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}