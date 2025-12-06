package com.example.center_management.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Thống kê tình hình mở khóa học theo năm:
 * - Thông tin khóa học
 * - Tổng số học viên
 * - Số lượng đạt / không đạt
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseYearStatisticResponse {

    private Long courseId;
    private String courseTitle;
    private LocalDate startDate;

    private Long totalStudents;
    private Long passedCount;
    private Long failedCount;
}
