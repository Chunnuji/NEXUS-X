package com.example.InventoryService.DTO;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryStatus {

    private Long productId;
    private Integer availableQuantity;
    private Integer reservedQuantity;
}
