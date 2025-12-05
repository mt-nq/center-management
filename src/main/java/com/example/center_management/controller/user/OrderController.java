package com.example.center_management.controller.user;

import com.example.center_management.dto.request.OrderCreateRequest;
import com.example.center_management.dto.response.OrderResponse;
import com.example.center_management.service.OrderService;
import com.example.center_management.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final StudentService studentService;

    // POST /api/orders  -> tạo order cho student đang login
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            Authentication authentication,
            @Valid @RequestBody OrderCreateRequest request
    ) {
        String username = authentication.getName();
        Long studentId = studentService.findStudentIdByUsername(username);

        OrderResponse response = orderService.createOrder(studentId, request);

        return ResponseEntity.ok(response);
    }

    // GET /api/orders/me  -> danh sách đơn của chính mình
    @GetMapping("/me")
    public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication authentication) {
        String username = authentication.getName();
        Long studentId = studentService.findStudentIdByUsername(username);
        return ResponseEntity.ok(orderService.getOrdersByStudent(studentId));
    }
}
