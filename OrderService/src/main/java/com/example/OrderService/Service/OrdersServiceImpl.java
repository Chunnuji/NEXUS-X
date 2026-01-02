package com.example.OrderService.Service;

import com.example.OrderService.DTO.*;
import com.example.OrderService.Dao.OrderRepository;
import com.example.OrderService.Entity.Orders;
import com.example.OrderService.KafkaConfig.OrderEventProducer;
import com.example.OrderService.Util.AuthorizationHeaderProvider;
import com.example.common.dto.UserPrincipal;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.OrderService.Entity.OrderStatus.*;

@Service
public class OrdersServiceImpl implements OrdersService{

    private final OrderRepository orderRepository;
    private final AuthorizationHeaderProvider authHeaderProvider;
    private final OrderEventProducer orderEventProducer;
    private final RestTemplate restTemplate;

    @Autowired
    public OrdersServiceImpl(OrderRepository orderRepository, AuthorizationHeaderProvider authHeaderProvider, OrderEventProducer orderEventProducer, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.authHeaderProvider = authHeaderProvider;
        this.orderEventProducer = orderEventProducer;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<OrderResponse> getOrder() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        Integer userId = principal.id();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        List<Orders> ordersList = new ArrayList<>();
        if (isAdmin) {
            ordersList = orderRepository.findAll();
        }else{
            ordersList = orderRepository.findByUserId(String.valueOf(userId));
        }

        List<OrderResponse> orderResponseList = new ArrayList<>();
        if(!ordersList.isEmpty()){
            for(Orders o: ordersList){
                OrderResponse orderResponse = new OrderResponse();
                BeanUtils.copyProperties(o,orderResponse);
                orderResponseList.add(orderResponse);
            }
        }
        return orderResponseList;
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        Orders orders = orderRepository.findByOrderId(orderId);
        OrderResponse orderResponse = new OrderResponse();
        BeanUtils.copyProperties(orders,orderResponse);
        return orderResponse;
    }

    @Override
    public OrderResponse cancelOrder(Long orderId) {
        Orders orders = orderRepository.findByOrderId(orderId);
        orders.setStatus(CANCELLED);
        Orders orders1 = orderRepository.save(orders);
        //kafka event
        orderEventProducer.sendOrderCanceledEvent(
                new OrderCanceledEvent(
                        orders1.getOrderId(),
                        orders1.getProductId(),
                        orders1.getQuantity(),
                        orders1.getTotalPrice(),
                        CANCELLED.name()
                )
        );
        OrderResponse orderResponse = new OrderResponse();
        BeanUtils.copyProperties(orders1,orderResponse);
        return orderResponse;
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Orders orders = new Orders();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        Integer userId = principal.id();

        BeanUtils.copyProperties(orderRequest,orders);
        orders.setUserId(String.valueOf(userId));
        orders.setCreatedAt(LocalDate.now());
        orders.setUpdatedAt(LocalDate.now());
        orders.setStatus(CREATED);
        BigDecimal totalPrice = BigDecimal.valueOf(orderRequest.getQuantity()).multiply(orderRequest.getUnitPrice());
        orders.setTotalPrice(totalPrice);
        Orders responseOrder = orderRepository.save(orders);

        //Inventory Update
        InventoryRequest inventoryRequest = new InventoryRequest(
                responseOrder.getOrderId(),
                responseOrder.getProductId(),
                responseOrder.getQuantity());

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION,
                authHeaderProvider.getAuthorizationHeader());

        HttpEntity<InventoryRequest> entity = new HttpEntity<>(inventoryRequest, headers);
        ResponseEntity<InventoryStatus> response =
                restTemplate.exchange(
                        "http://localhost:8084/inventory/reserveStock",
                        HttpMethod.POST,
                        entity,
                        InventoryStatus.class
                );

        InventoryStatus inventoryStatus = response.getBody();

        //kafka event
        orderEventProducer.sendOrderCreatedEvent(
                new OrderCreatedEvent(
                        responseOrder.getOrderId(),
                        responseOrder.getProductId(),
                        responseOrder.getQuantity(),
                        totalPrice,
                        CREATED.name()
                )
        );


        OrderResponse orderResponse = new OrderResponse();
        BeanUtils.copyProperties(responseOrder,orderResponse);
        return orderResponse;
    }

    @Override
    public OrderResponse completeOrder(Long orderId) {
        Orders orders = orderRepository.findByOrderId(orderId);
        orders.setStatus(COMPLETED);
        Orders orders1 = orderRepository.save(orders);
        //kafka event
        orderEventProducer.sendOrderCompletedEvent(
                new OrderCompletedEvent(
                        orders1.getOrderId(),
                        orders1.getProductId(),
                        orders1.getQuantity(),
                        orders1.getTotalPrice(),
                        COMPLETED.name()
                )
        );
        OrderResponse orderResponse = new OrderResponse();
        BeanUtils.copyProperties(orders1,orderResponse);
        return orderResponse;
    }
}
