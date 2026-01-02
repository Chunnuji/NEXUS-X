package com.example.InventoryService.KafkaConfig;

import com.example.InventoryService.DTO.OrderCanceledEvent;
import com.example.InventoryService.DTO.OrderCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private static final String ORDER_CREATED_TOPIC = "orders-created";
    private static final String ORDER_COMPLETED_TOPIC = "orders-completed";
    private static final String ORDER_CANCELLED_TOPIC = "orders-cancelled";

    private final KafkaTemplate<String, Object> kafkaTemplateCompleted;
    private final KafkaTemplate<String, Object> kafkaTemplateCanceled;


    public void sendOrderCompletedEvent(OrderCompletedEvent event) {
        kafkaTemplateCompleted.send(
                ORDER_COMPLETED_TOPIC,
                event.getOrderId().toString(),
                event
        );
    }

    public void sendOrderCanceledEvent(OrderCanceledEvent event) {
        kafkaTemplateCanceled.send(
                ORDER_CANCELLED_TOPIC,
                event.getOrderId().toString(),
                event
        );
    }
}

