package com.example.PaymentService.KafkaConfig;

import com.example.PaymentService.DTO.OrderCreatedEvent;
import com.example.PaymentService.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final PaymentService paymentService;


    @KafkaListener(
            topics = "orders-created",
            containerFactory = "orderCreatedKafkaListenerFactory"
    )
    public void onOrderCreated(OrderCreatedEvent event) {
        paymentService.consumeOrderCreateEvent(event);
    }

}

