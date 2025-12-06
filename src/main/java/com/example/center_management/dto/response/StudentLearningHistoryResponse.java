package com.example.center_management.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Thống kê lịch sử học của 1 học viên
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentLearningHistoryResponse {

    // Thông tin học viên
    private Long studentId;
    private String studentCode;
    private String studentName;

    // Thống kê tổng quan
    private int totalEnrollments;      // tổng số khoá đã đăng ký
    private int completedEnrollments;  // số khoá COMPLETED
    private int certificateCount;      // số enrollment đã có certificate
    private int passedCount;           // số certificate PASS
    private int failedCount;           // số certificate FAIL
    private double averageProgress;    // % tiến độ trung bình (0–100)

    // Danh sách chi tiết từng khoá
    private List<EnrollmentResponse> enrollments;
}
