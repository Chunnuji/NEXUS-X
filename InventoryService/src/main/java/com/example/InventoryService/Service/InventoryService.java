package com.example.InventoryService.Service;


import com.example.InventoryService.DTO.InventoryRequest;
import com.example.InventoryService.DTO.InventoryStatus;

public interface InventoryService {

    InventoryStatus getInventoryStatus(Long productId);
    InventoryStatus reserveStock(InventoryRequest request);
    InventoryStatus releaseStock(InventoryRequest request);

}
