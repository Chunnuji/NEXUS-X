package com.example.OrderService.Service;

import com.example.OrderService.DTO.OrderRequest;
import com.example.OrderService.DTO.OrderResponse;
import com.example.OrderService.Dao.OrderRepository;
import com.example.OrderService.Entity.Orders;
import com.example.common.dto.UserPrincipal;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.OrderService.Entity.OrderStatus.*;

@Service
public class OrdersServiceImpl implements OrdersService{

    private final OrderRepository orderRepository;

    public OrdersServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
        OrderResponse orderResponse = new OrderResponse();
        BeanUtils.copyProperties(responseOrder,orderResponse);
        return orderResponse;
    }

    @Override
    public OrderResponse completeOrder(Long orderId) {
        Orders orders = orderRepository.findByOrderId(orderId);
        orders.setStatus(COMPLETED);
        Orders orders1 = orderRepository.save(orders);
        OrderResponse orderResponse = new OrderResponse();
        BeanUtils.copyProperties(orders1,orderResponse);
        return orderResponse;
    }
}
