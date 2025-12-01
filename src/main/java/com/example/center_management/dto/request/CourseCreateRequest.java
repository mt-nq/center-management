// CourseCreateRequest.java
package com.example.center_management.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CourseCreateRequest {

    private String courseName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}
