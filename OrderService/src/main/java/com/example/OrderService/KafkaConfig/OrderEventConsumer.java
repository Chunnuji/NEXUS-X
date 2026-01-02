package com.example.OrderService.KafkaConfig;

import com.example.OrderService.DTO.InventoryRequest;
import com.example.OrderService.DTO.OrderCanceledEvent;
import com.example.OrderService.DTO.OrderCompletedEvent;
import com.example.OrderService.DTO.OrderCreatedEvent;
import com.example.OrderService.Service.InventoryService;
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

