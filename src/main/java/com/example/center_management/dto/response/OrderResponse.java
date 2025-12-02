package com.example.center_management.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.center_management.domain.enums.PaymentStatus;

@Getter
@Setter
@Builder
public class OrderResponse {

    private Long id;

    private Long studentId;
    private String studentName;

    private Long courseId;
    private String courseTitle;

    private BigDecimal amount;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private String approvalStatus;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
}
