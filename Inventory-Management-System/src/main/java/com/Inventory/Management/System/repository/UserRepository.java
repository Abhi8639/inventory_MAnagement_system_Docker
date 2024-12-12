package com.Inventory.Management.System.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Inventory.Management.System.model.User;


public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
