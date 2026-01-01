package com.example.OrderService.Service;

import com.example.OrderService.DTO.OrderRequest;
import com.example.OrderService.DTO.OrderResponse;

import java.util.List;

public interface OrdersService {

    List<OrderResponse> getOrder();
    OrderResponse getOrderById(Long orderId);
    OrderResponse cancelOrder(Long orderId);
    OrderResponse createOrder(OrderRequest orderRequest);
    OrderResponse completeOrder(Long orderId);
}
