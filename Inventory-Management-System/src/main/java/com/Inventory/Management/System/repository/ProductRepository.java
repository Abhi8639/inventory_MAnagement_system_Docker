package com.Inventory.Management.System.repository;

import com.Inventory.Management.System.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Product findByName(String name);
}
