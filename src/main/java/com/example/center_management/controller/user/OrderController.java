package com.example.center_management.controller.user;

import com.example.center_management.dto.request.OrderCreateRequest;
import com.example.center_management.dto.response.OrderResponse;
import com.example.center_management.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // STUDENT: bấm nút Thanh toán -> tạo order
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderCreateRequest request
    ) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.ok(response);
    }

    // STUDENT: xem các đơn của mình
    @GetMapping("/me/{studentId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStudent(
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(orderService.getOrdersByStudent(studentId));
    }
}
