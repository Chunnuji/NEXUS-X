package com.example.OrderService.DTO;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    private Long orderId;
    private Long productId;
    private Integer quantity;
}
