package com.example.center_management.dto.enrollment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentCompletionResponse {

    private Long enrollmentId;
    private Long studentId;
    private Long courseId;
    private boolean completed; // true nếu status = COMPLETED
}
