package com.example.PaymentService.Dao;

import com.example.PaymentService.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {

    Payment findByOrderId(Long orderId);
}
