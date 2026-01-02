package com.example.InventoryService.KafkaConfig;

import com.example.InventoryService.DTO.InventoryRequest;
import com.example.InventoryService.DTO.OrderCanceledEvent;
import com.example.InventoryService.DTO.OrderCompletedEvent;
import com.example.InventoryService.Service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "orders-cancelled",
            containerFactory = "orderCancelledKafkaListenerFactory"
    )
    public void onOrderCancelled(OrderCanceledEvent event) {
        InventoryRequest request = new InventoryRequest();
        BeanUtils.copyProperties(event, request);
        System.out.println("Order cancelled event consumed");
        inventoryService.releaseStock(request);
    }

    @KafkaListener(
            topics = "orders-completed",
            containerFactory = "orderCompletedKafkaListenerFactory"
    )
    public void onOrderCompleted(OrderCompletedEvent event) {
        InventoryRequest request = new InventoryRequest();
        BeanUtils.copyProperties(event, request);
        inventoryService.reserveStock(request);
    }

}

