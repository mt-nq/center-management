package com.example.center_management.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseResponse {

    private Long id;
    private String code;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String content;
    private String status;
    private BigDecimal price;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
