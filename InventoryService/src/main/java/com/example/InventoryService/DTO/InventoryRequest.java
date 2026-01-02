package com.example.InventoryService.DTO;

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
