package com.example.center_management.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentResultUpdateRequest {
    /**
     * true  = Đạt (PASSED)
     * false = Không đạt (FAILED)
     */
    private Boolean passed;
}
