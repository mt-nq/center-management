package com.example.center_management.service.impl;

import com.example.center_management.domain.entity.Course;
import com.example.center_management.domain.entity.Order;
import com.example.center_management.domain.entity.Student;
import com.example.center_management.dto.request.EnrollmentCreateRequest;
import com.example.center_management.dto.request.OrderCreateRequest;
import com.example.center_management.dto.response.OrderResponse;
import com.example.center_management.exception.BadRequestException;
import com.example.center_management.exception.ResourceNotFoundException;
import com.example.center_management.repository.CourseRepository;
import com.example.center_management.repository.OrderRepository;
import com.example.center_management.repository.StudentRepository;
import com.example.center_management.service.EnrollmentService;
import com.example.center_management.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentService enrollmentService;

    // ================== STUDENT: TẠO ĐƠN HÀNG ==================
    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Order order = Order.builder()
                .student(student)
                .course(course)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .approvalStatus("PENDING")
                .build();

        order = orderRepository.save(order);
        return toResponse(order);
    }

    // ================== ADMIN: LẤY ĐƠN CHỜ DUYỆT ==================
    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getPendingOrders() {
        return orderRepository.findByApprovalStatus("PENDING")
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ================== ADMIN: DUYỆT ĐƠN ==================
    @Override
    @Transactional
    public OrderResponse approveOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if ("APPROVED".equalsIgnoreCase(order.getApprovalStatus())) {
            throw new BadRequestException("Order already approved");
        }
        if ("REJECTED".equalsIgnoreCase(order.getApprovalStatus())) {
            throw new BadRequestException("Order already rejected");
        }

        // Khi duyệt -> tạo Enrollment tương ứng
        EnrollmentCreateRequest enrollReq = new EnrollmentCreateRequest();
        enrollReq.setStudentId(order.getStudent().getId());
        enrollReq.setCourseId(order.getCourse().getId());
        enrollmentService.enroll(enrollReq);

        order.setApprovalStatus("APPROVED");
        order.setApprovedAt(LocalDateTime.now());

        order = orderRepository.save(order);
        return toResponse(order);
    }

    // ================== ADMIN: TỪ CHỐI ĐƠN ==================
    @Override
    @Transactional
    public OrderResponse rejectOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if ("APPROVED".equalsIgnoreCase(order.getApprovalStatus())) {
            throw new BadRequestException("Order already approved");
        }
        if ("REJECTED".equalsIgnoreCase(order.getApprovalStatus())) {
            throw new BadRequestException("Order already rejected");
        }

        order.setApprovalStatus("REJECTED");
        order.setRejectedAt(LocalDateTime.now());

        order = orderRepository.save(order);
        return toResponse(order);
    }

    // ================== HELPER ==================
    private OrderResponse toResponse(Order o) {
        return OrderResponse.builder()
                .id(o.getId())
                .studentId(o.getStudent().getId())
                .studentName(o.getStudent().getFullName())
                .courseId(o.getCourse().getId())
                .courseTitle(o.getCourse().getTitle())
                .amount(o.getAmount())
                .paymentMethod(o.getPaymentMethod())
                .approvalStatus(o.getApprovalStatus())
                .createdAt(o.getCreatedAt())
                .approvedAt(o.getApprovedAt())
                .rejectedAt(o.getRejectedAt())
                .build();
    }
}
