package com.example.OrderService.Dao;

import com.example.OrderService.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders,Integer> {
    List<Orders> findByUserId(String userId);
    Orders findByOrderId(Long orderId);

}
