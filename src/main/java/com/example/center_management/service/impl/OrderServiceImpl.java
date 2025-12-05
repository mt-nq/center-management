package com.example.center_management.service.impl;

import com.example.center_management.domain.entity.Course;
import com.example.center_management.domain.entity.Order;
import com.example.center_management.domain.entity.Student;
import com.example.center_management.domain.enums.ApprovalStatus;
import com.example.center_management.domain.enums.PaymentStatus;
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
    public OrderResponse createOrder(Long studentId, OrderCreateRequest request) {
        // Lấy student từ id đã resolve từ JWT
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Order order = new Order();
        order.setStudent(student);
        order.setCourse(course);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setApprovalStatus(ApprovalStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);

        return toResponse(order);
    }


    // ================== ADMIN: LẤY ĐƠN CHỜ DUYỆT ==================
    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getPendingOrders() {
        return orderRepository.findByApprovalStatus(ApprovalStatus.PENDING)
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

        ApprovalStatus currentStatus = order.getApprovalStatus();

        if (currentStatus == ApprovalStatus.APPROVED) {
            throw new BadRequestException("Order already approved");
        }
        if (currentStatus == ApprovalStatus.REJECTED) {
            throw new BadRequestException("Order already rejected");
        }

        // Khi duyệt -> tạo Enrollment tương ứng
        EnrollmentCreateRequest enrollReq = new EnrollmentCreateRequest();
        enrollReq.setStudentId(order.getStudent().getId());
        enrollReq.setCourseId(order.getCourse().getId());
        enrollmentService.enroll(enrollReq);

        order.setApprovalStatus(ApprovalStatus.APPROVED);
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

        ApprovalStatus currentStatus = order.getApprovalStatus();

        if (currentStatus == ApprovalStatus.APPROVED) {
            throw new BadRequestException("Order already approved");
        }
        if (currentStatus == ApprovalStatus.REJECTED) {
            throw new BadRequestException("Order already rejected");
        }

        order.setApprovalStatus(ApprovalStatus.REJECTED);
        order.setRejectedAt(LocalDateTime.now());

        order = orderRepository.save(order);
        return toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStudent(Long studentId) {
        return orderRepository.findByStudent_Id(studentId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ================== HELPER ==================
    private OrderResponse toResponse(Order order) {
        OrderResponse res = new OrderResponse();
        res.setId(order.getId());

        if (order.getStudent() != null) {
            res.setStudentId(order.getStudent().getId());
            res.setStudentName(order.getStudent().getFullName());
        }

        if (order.getCourse() != null) {
            res.setCourseId(order.getCourse().getId());
            res.setCourseTitle(order.getCourse().getTitle());
        }

        // 👇 KHỚP VỚI ENTITY Order: dùng amount thay vì totalAmount
        res.setTotalAmount(order.getAmount());

        res.setPaymentStatus(order.getPaymentStatus());
        res.setApprovalStatus(order.getApprovalStatus());
        res.setCreatedAt(order.getCreatedAt());
        res.setApprovedAt(order.getApprovedAt());
        res.setRejectedAt(order.getRejectedAt());

        String studentPart = order.getStudent() != null
                ? order.getStudent().getId().toString()
                : "UNKNOWN";

        res.setTransferNote("ORDER-" + order.getId() + "-STUDENT-" + studentPart);

        return res;
    }
}
