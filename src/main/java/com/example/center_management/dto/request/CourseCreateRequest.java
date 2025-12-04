package com.example.center_management.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseCreateRequest {

    @NotBlank
    private String title;      // bắt buộc có tên khóa học

    // Có thể null, chỉ validate nếu cả startDate & endDate đều khác null
    private LocalDate startDate;

    private LocalDate endDate;

    private String content;

    private BigDecimal price;
}
