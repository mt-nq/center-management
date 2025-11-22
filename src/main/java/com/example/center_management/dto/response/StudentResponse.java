package com.example.center_management.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentResponse {

    private Long id;
    private String code;
    private String fullName;
    private LocalDate dob;
    private String hometown;
    private String province;
    private String status;
    private LocalDateTime createdAt;
}
