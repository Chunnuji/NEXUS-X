package com.example.PaymentService.DTO;

import com.example.PaymentService.Entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private BigDecimal amount;
    private PaymentStatus paymentStatus;
    private String paymentMethod;
    private String transactionId;
    private String gatewayReference;
}
