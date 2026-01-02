package com.example.PaymentService.Service;

import com.example.PaymentService.DTO.*;
import com.example.PaymentService.Dao.PaymentRepository;
import com.example.PaymentService.Entity.Payment;
import com.example.PaymentService.Entity.PaymentStatus;
import com.example.PaymentService.KafkaConfig.OrderEventProducer;
import com.example.PaymentService.Util.TransactionIdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import static com.example.PaymentService.Entity.OrderStatus.CANCELLED;
import static com.example.PaymentService.Entity.OrderStatus.COMPLETED;

@Service
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final TransactionIdGenerator transactionIdGenerator;
    private final OrderEventProducer orderEventProducer;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, TransactionIdGenerator transactionIdGenerator, OrderEventProducer orderEventProducer) {
        this.paymentRepository = paymentRepository;
        this.transactionIdGenerator = transactionIdGenerator;
        this.orderEventProducer = orderEventProducer;
    }

    @Override
    public PaymentResponse getPaymentStatus(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId);
        PaymentResponse paymentResponse = new PaymentResponse();
        BeanUtils.copyProperties(payment,paymentResponse);
        return paymentResponse;
    }

    @Override
    public void consumeOrderCreateEvent(OrderCreatedEvent orderCreatedEvent) {
        Payment payment = new Payment();
        payment.setOrderId(orderCreatedEvent.getOrderId());
        payment.setProductId(orderCreatedEvent.getProductId());
        payment.setAmount(orderCreatedEvent.getTotalPrice());
        payment.setQuantity(orderCreatedEvent.getQuantity());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
    }

    @Override
    public PaymentResponse doPayment(PaymentRequest paymentRequest) {
        Payment payment = paymentRepository.findByOrderId(paymentRequest.getOrderId());
        if(paymentRequest.isStatus()){
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setPaymentMethod(paymentRequest.getPaymentMethod());
            payment.setGatewayReference(paymentRequest.getGatewayReference());
            payment.setTransactionId(transactionIdGenerator.generate());
            payment.setUpdatedAt(LocalDateTime.now());
        }

        Payment payment1 = paymentRepository.save(payment);

        //kafka event
        if(payment1.getPaymentStatus()==PaymentStatus.SUCCESS){
            orderEventProducer.sendOrderCompletedEvent(
                    new OrderCompletedEvent(
                            payment1.getOrderId(),
                            payment1.getProductId(),
                            payment1.getQuantity(),
                            payment1.getAmount(),
                            COMPLETED.name()
                    )
            );
        }
        PaymentResponse paymentResponse = new PaymentResponse();
        BeanUtils.copyProperties(payment1,paymentResponse);
        return paymentResponse;
    }

    @Override
    public PaymentResponse cancelPayment(PaymentRequest paymentRequest) {

        Payment payment = paymentRepository.findByOrderId(paymentRequest.getOrderId());

        if (payment == null) {
            throw new RuntimeException("Payment not found for orderId: " + paymentRequest.getOrderId());
        }

        // ✅ Always mark as CANCELLED / FAILED
        payment.setPaymentStatus(PaymentStatus.FAILED);
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setGatewayReference(paymentRequest.getGatewayReference());
        payment.setUpdatedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        // ✅ Always emit cancel event
        orderEventProducer.sendOrderCanceledEvent(
                new OrderCanceledEvent(
                        savedPayment.getOrderId(),
                        savedPayment.getProductId(),
                        savedPayment.getQuantity(),
                        savedPayment.getAmount(),
                        CANCELLED.name()
                )
        );

        PaymentResponse response = new PaymentResponse();
        BeanUtils.copyProperties(savedPayment, response);
        return response;
    }

}
