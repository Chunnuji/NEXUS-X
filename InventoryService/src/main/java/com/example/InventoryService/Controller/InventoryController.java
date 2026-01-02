package com.example.InventoryService.Controller;

import com.example.InventoryService.DTO.InventoryRequest;
import com.example.InventoryService.DTO.InventoryStatus;
import com.example.InventoryService.Service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    public final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/getByProductId/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<InventoryStatus> getByProductId(@PathVariable Long productId){
        InventoryStatus inventoryStatus = inventoryService.getInventoryStatus(productId);
        return ResponseEntity.ok(inventoryStatus);
    }

    @PostMapping("/reserveStock")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<InventoryStatus> reserveStock(@RequestBody InventoryRequest inventoryRequest){
        InventoryStatus inventoryStatus = inventoryService.reserveStock(inventoryRequest);
        return ResponseEntity.ok(inventoryStatus);
    }

    @PostMapping("/releaseStock")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<InventoryStatus> releaseStock(@RequestBody InventoryRequest inventoryRequest){
        InventoryStatus inventoryStatus = inventoryService.releaseStock(inventoryRequest);
        return ResponseEntity.ok(inventoryStatus);
    }
}
