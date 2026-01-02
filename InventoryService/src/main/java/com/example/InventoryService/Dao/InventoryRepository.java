package com.example.InventoryService.Dao;


import com.example.InventoryService.Entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory,Integer> {

    Inventory findByProductId(Long productId);

}
