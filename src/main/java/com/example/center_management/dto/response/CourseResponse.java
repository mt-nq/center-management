// CourseResponse.java
package com.example.center_management.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CourseResponse {

    private Integer id;
    private String courseName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}
