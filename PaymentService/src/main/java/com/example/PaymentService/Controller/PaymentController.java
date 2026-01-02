package com.example.PaymentService.Controller;


import com.example.PaymentService.DTO.PaymentRequest;
import com.example.PaymentService.DTO.PaymentResponse;
import com.example.PaymentService.Service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/paymentStatus/{orderId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<PaymentResponse> paymentCompleted(@PathVariable Long orderId){
        PaymentResponse paymentResponse = paymentService.getPaymentStatus(orderId);
        return ResponseEntity.ok(paymentResponse);
    }

    @PostMapping("/paymentComplete")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<PaymentResponse> paymentComplete(@RequestBody PaymentRequest paymentRequest){
        PaymentResponse paymentResponse = paymentService.doPayment(paymentRequest);
        return ResponseEntity.ok(paymentResponse);
    }

    @PostMapping("/paymentCancel")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<PaymentResponse> paymentCancel(@RequestBody PaymentRequest paymentRequest){
        PaymentResponse paymentResponse = paymentService.cancelPayment(paymentRequest);
        return ResponseEntity.ok(paymentResponse);
    }
}
