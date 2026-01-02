package com.example.PaymentService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    Long orderId;
    String paymentMethod;
    String gatewayReference;
    boolean status;

}
