// src/main/java/com/example/center_management/dto/response/OrderResponse.java
package com.example.center_management.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor      // 👈 thêm
@AllArgsConstructor     // 👈 constructor full args
public class OrderResponse {

    private Long id;

    private Long studentId;
    private String studentName;

    private Long courseId;
    private String courseTitle;

    private BigDecimal amount;
    private String paymentMethod;

    private String paymentStatus;    // nếu chưa có thì thêm luôn
    private String approvalStatus;   // PENDING / APPROVED / REJECTED

    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
}
