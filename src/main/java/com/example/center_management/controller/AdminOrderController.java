package com.example.center_management.controller;

import com.example.center_management.dto.response.OrderResponse;
import com.example.center_management.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    // ========== GET /admin/orders/pending ==========
    @GetMapping("/pending")
    public List<OrderResponse> getPendingOrders() {
        return orderService.getPendingOrders();
    }

    // ========== PUT /admin/orders/{id}/approve ==========
    @PutMapping("/{id}/approve")
    public OrderResponse approve(@PathVariable Long id) {
        return orderService.approveOrder(id);
    }

    // ========== PUT /admin/orders/{id}/reject ==========
    @PutMapping("/{id}/reject")
    public OrderResponse reject(@PathVariable Long id) {
        return orderService.rejectOrder(id);
    }
}
