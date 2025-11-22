package com.example.center_management.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentResponse {

    private Long id;
    private Long studentId;
    private String studentCode;
    private String studentName;

    private Long courseId;
    private String courseCode;
    private String courseTitle;

    private LocalDateTime enrolledAt;
    private String result;
    private String certificateNo;
}
