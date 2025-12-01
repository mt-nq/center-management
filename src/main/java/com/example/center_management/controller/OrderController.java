package com.example.center_management.controller;

import com.example.center_management.dto.request.OrderCreateRequest;
import com.example.center_management.dto.response.EnrollmentResponse;
import com.example.center_management.dto.response.OrderResponse;
import com.example.center_management.service.EnrollmentService;
import com.example.center_management.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final EnrollmentService enrollmentService;

    // ========== POST /orders – Tạo đơn sau khi thanh toán ==========
    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody OrderCreateRequest request) {
        return orderService.createOrder(request);
    }

    // ========== GET /enrollments/me – Danh sách khóa học đã kích hoạt ==========
    // Ở đây mình cho truyền studentId qua query param cho đơn giản (chưa dùng token)
    @GetMapping("/enrollments/me")
    public List<EnrollmentResponse> getMyEnrollments(
            @RequestParam("studentId") Long studentId
    ) {
        return enrollmentService.getByStudent(studentId);
    }
}
