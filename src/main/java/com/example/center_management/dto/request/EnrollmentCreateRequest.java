package com.example.center_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentCreateRequest {

    @NotNull
    private Long studentId;

    @NotNull
    private Long courseId;

    private Long orderId;
}
