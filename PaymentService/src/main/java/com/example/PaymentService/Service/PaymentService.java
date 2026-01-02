package com.example.PaymentService.Service;

import com.example.PaymentService.DTO.OrderCreatedEvent;
import com.example.PaymentService.DTO.PaymentRequest;
import com.example.PaymentService.DTO.PaymentResponse;

public interface PaymentService {

    PaymentResponse getPaymentStatus(Long orderId);
    void consumeOrderCreateEvent(OrderCreatedEvent orderCreatedEvent);
    PaymentResponse doPayment(PaymentRequest paymentRequest);
    PaymentResponse cancelPayment(PaymentRequest paymentRequest);
}
