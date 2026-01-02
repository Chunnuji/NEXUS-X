package com.example.OrderService.Service;

import com.example.OrderService.DTO.InventoryRequest;
import com.example.OrderService.DTO.InventoryStatus;

public interface InventoryService {

    InventoryStatus getInventoryStatus(Long productId);
    InventoryStatus reserveStock(InventoryRequest request);
    InventoryStatus releaseStock(InventoryRequest request);

}
