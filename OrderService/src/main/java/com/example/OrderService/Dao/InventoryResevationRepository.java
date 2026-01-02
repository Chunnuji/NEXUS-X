package com.example.OrderService.Dao;

import com.example.OrderService.Entity.InventoryReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryResevationRepository extends JpaRepository<InventoryReservation,Integer> {
    Optional<InventoryReservation> findByOrderId(Long orderId);
}
