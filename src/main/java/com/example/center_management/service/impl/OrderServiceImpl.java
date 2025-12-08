package com.example.center_management.service.impl;

import com.example.center_management.domain.entity.Course;
import com.example.center_management.domain.entity.Order;
import com.example.center_management.domain.entity.Student;
import com.example.center_management.domain.enums.ApprovalStatus;
import com.example.center_management.domain.enums.CompletionResult;
import com.example.center_management.domain.enums.EnrollmentStatus;
import com.example.center_management.domain.enums.PaymentStatus;
import com.example.center_management.dto.request.EnrollmentCreateRequest;
import com.example.center_management.dto.request.OrderCreateRequest;
import com.example.center_management.dto.response.OrderResponse;
import com.example.center_management.exception.BadRequestException;
import com.example.center_management.exception.ResourceNotFoundException;
import com.example.center_management.repository.CourseRepository;
import com.example.center_management.repository.EnrollmentRepository;
import com.example.center_management.repository.OrderRepository;
import com.example.center_management.repository.StudentRepository;
import com.example.center_management.service.EnrollmentService;
import com.example.center_management.service.OrderService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentService enrollmentService;
    private final EnrollmentRepository enrollmentRepository;

    // ================== STUDENT: TẠO ĐƠN HÀNG ==================
@Override
public OrderResponse createOrder(Long studentId, OrderCreateRequest request) {

    Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

    Course course = courseRepository.findById(request.getCourseId())
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

    List<Order> existingOrders = orderRepository.findByStudentIdAndCourseId(studentId, course.getId());

    for (Order existingOrder : existingOrders) {

        // 1) Đơn đang chờ duyệt
        if (existingOrder.getApprovalStatus() == ApprovalStatus.PENDING) {
            throw new BadRequestException("Your previous order for this course is still pending.");
        }

        // 2) Đơn đã duyệt -> kiểm tra enrollment
        if (existingOrder.getApprovalStatus() == ApprovalStatus.APPROVED) {

            boolean hasActiveEnrollment = enrollmentRepository.existsByStudentIdAndCourseIdAndStatusIn(
                    studentId,
                    course.getId(),
                    List.of(EnrollmentStatus.ENROLLED, EnrollmentStatus.COMPLETE)
            );

            if (hasActiveEnrollment) {
                throw new BadRequestException("You already have an active or completed enrollment for this course.");
            }
        }

        // 3) Check completionResult (nếu bạn lưu trên Order)
        CompletionResult result = existingOrder.getCompletionResult();
        if (result == CompletionResult.PASSED || result == CompletionResult.NOT_REVIEWED) {
            throw new BadRequestException("You cannot purchase this course again.");
        }

        // Nếu FAILED -> cho phép mua lại
    }

    // Tạo đơn mới
    Order order = new Order();
    order.setStudent(student);
    order.setCourse(course);
    order.setPaymentStatus(PaymentStatus.PENDING);
    order.setApprovalStatus(ApprovalStatus.PENDING);
    order.setCreatedAt(LocalDateTime.now());
    order.setPrice(course.getPrice());

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

    // ================== ADMIN: LẤY TẤT CẢ ĐƠN (CÓ PHÂN TRANG) ==================
    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findAll(pageable);
        // Dùng lại hàm toResponse cho đồng nhất
        return orders.map(this::toResponse);
    }

    // ================== HELPER ==================
    private OrderResponse toResponse(Order order) {
        OrderResponse res = new OrderResponse();
        res.setId(order.getId());

        if (order.getStudent() != null) {
            res.setStudentId(order.getStudent().getId());
            res.setStudentName(order.getStudent().getFullName());
            // nếu OrderResponse có studentCode thì set thêm ở đây
            // res.setStudentCode(order.getStudent().getStudentCode());
        }

        if (order.getCourse() != null) {
            res.setCourseId(order.getCourse().getId());
            res.setCourseTitle(order.getCourse().getTitle());
            // nếu OrderResponse có courseCode thì set thêm ở đây
            // res.setCourseCode(order.getCourse().getCourseCode());
        }

        // entity của bạn đang dùng price
        res.setPrice(order.getPrice());

        res.setPaymentStatus(order.getPaymentStatus());
        res.setApprovalStatus(order.getApprovalStatus());
        res.setCreatedAt(order.getCreatedAt());
        res.setApprovedAt(order.getApprovedAt());
        res.setRejectedAt(order.getRejectedAt());

        String studentPart = order.getStudent() != null
                ? order.getStudent().getId().toString()
                : "UNKNOWN";

        // Gợi ý nội dung chuyển khoản
        res.setTransferNote("ORDER-" + order.getId() + "-STUDENT-" + studentPart);

        return res;
    }
}
