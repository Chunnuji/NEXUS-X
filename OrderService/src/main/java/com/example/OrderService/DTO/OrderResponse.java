package com.example.OrderService.DTO;

import com.example.OrderService.Entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderResponse {
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private String userId;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
