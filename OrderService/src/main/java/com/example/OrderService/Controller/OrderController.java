package com.example.OrderService.Controller;

import com.example.OrderService.DTO.OrderRequest;
import com.example.OrderService.DTO.OrderResponse;
import com.example.OrderService.Service.OrdersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrdersService ordersService;

    public OrderController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping("/getOrderById/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id){
        OrderResponse response = ordersService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getOrders")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<OrderResponse>> getOrders(){
        List<OrderResponse> response = ordersService.getOrder();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cancelOrder/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id){
        OrderResponse response = ordersService.cancelOrder(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/createOrder")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<OrderResponse> order(@RequestBody OrderRequest orderRequest){
        OrderResponse orderResponse = ordersService.createOrder(orderRequest);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/complete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<OrderResponse> orderComplete(@PathVariable Long id){
        OrderResponse orderResponse = ordersService.completeOrder(id);
        return ResponseEntity.ok(orderResponse);
    }


}
