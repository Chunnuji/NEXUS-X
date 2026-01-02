package com.example.OrderService.Controller;

import com.example.OrderService.DTO.InventoryRequest;
import com.example.OrderService.DTO.InventoryStatus;
import com.example.OrderService.Service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    public final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/getByProductId/{id}")
    public ResponseEntity<InventoryStatus> getByProductId(@PathVariable Long productId){
        InventoryStatus inventoryStatus = inventoryService.getInventoryStatus(productId);
        return ResponseEntity.ok(inventoryStatus);
    }

    @PostMapping("/reserveStock")
    public ResponseEntity<InventoryStatus> reserveStock(@RequestBody InventoryRequest inventoryRequest){
        InventoryStatus inventoryStatus = inventoryService.reserveStock(inventoryRequest);
        return ResponseEntity.ok(inventoryStatus);
    }

    @PostMapping("/releaseStock")
    public ResponseEntity<InventoryStatus> releaseStock(@RequestBody InventoryRequest inventoryRequest){
        InventoryStatus inventoryStatus = inventoryService.releaseStock(inventoryRequest);
        return ResponseEntity.ok(inventoryStatus);
    }
}
