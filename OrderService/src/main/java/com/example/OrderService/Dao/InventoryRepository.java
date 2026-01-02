package com.example.OrderService.Dao;

import com.example.OrderService.Entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory,Integer> {

    Inventory findByProductId(Long productId);

}
