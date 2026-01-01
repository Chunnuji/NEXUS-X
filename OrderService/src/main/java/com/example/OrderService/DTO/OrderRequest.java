package com.example.OrderService.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderRequest {

    private Long productId;
    private Integer quantity;
//    private String userId;
    private BigDecimal unitPrice;
}
