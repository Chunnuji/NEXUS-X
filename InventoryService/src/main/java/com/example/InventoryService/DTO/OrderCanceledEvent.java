package com.example.InventoryService.DTO;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCanceledEvent {
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String status;
}

