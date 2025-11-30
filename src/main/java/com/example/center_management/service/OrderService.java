package com.example.center_management.service;

import com.example.center_management.dto.request.OrderCreateRequest;
import com.example.center_management.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    // POST /orders
    OrderResponse createOrder(OrderCreateRequest request);

    // GET /admin/orders/pending
    List<OrderResponse> getPendingOrders();

    // PUT /admin/orders/{id}/approve
    OrderResponse approveOrder(Long orderId);

    // PUT /admin/orders/{id}/reject
    OrderResponse rejectOrder(Long orderId);
}
