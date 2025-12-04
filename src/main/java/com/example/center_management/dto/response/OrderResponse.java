package com.example.center_management.dto.response;

import com.example.center_management.domain.enums.ApprovalStatus;
import com.example.center_management.domain.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;

    private Long studentId;
    private String studentName;

    private Long courseId;
    private String courseTitle;

    private BigDecimal totalAmount;         // khớp Order.amount

    private PaymentStatus paymentStatus;    // enum
    private ApprovalStatus approvalStatus;  // enum

    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;

    private String transferNote;
}
