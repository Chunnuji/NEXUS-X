package com.example.OrderService.DTO;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {

    private Long orderId;
    private Long productId;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String status;
}

