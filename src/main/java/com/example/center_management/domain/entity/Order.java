package com.example.center_management.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Học viên đặt mua
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // Khóa học
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // Số tiền (nếu chưa có có thể để null)
    @Column(precision = 15, scale = 2)
    private BigDecimal amount;

    // Phương thức thanh toán (VNPAY, MOMO, CASH,…)
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    // PENDING / APPROVED / REJECTED
    @Column(name = "approval_status", length = 20, nullable = false)
    private String approvalStatus;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;
}
