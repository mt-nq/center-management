// ProgressResponse.java
package com.example.center_management.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProgressResponse {

    private Integer enrollmentId;
    private int totalLessons;
    private int completedLessons;
    private double percentage;
}
