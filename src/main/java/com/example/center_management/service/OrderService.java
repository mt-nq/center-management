package com.example.center_management.service;

import com.example.center_management.dto.request.OrderCreateRequest;
import com.example.center_management.dto.response.OrderResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    // POST /orders
    OrderResponse createOrder(Long studentId, OrderCreateRequest request);

    // GET /admin/orders/pending
    List<OrderResponse> getPendingOrders();

    List<OrderResponse> getApprovedOrders();

    List<OrderResponse> getRejectedOrders();

    // PUT /admin/orders/{id}/approve
    OrderResponse approveOrder(Long orderId);

    // PUT /admin/orders/{id}/reject
    OrderResponse rejectOrder(Long orderId);

    List<OrderResponse> getOrdersByStudent(Long studentId);

    Page<OrderResponse> getAllOrders(int page, int size);

}
