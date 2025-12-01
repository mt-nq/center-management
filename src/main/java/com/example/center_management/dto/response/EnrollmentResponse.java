// EnrollmentResponse.java
package com.example.center_management.dto.response;

import com.example.center_management.domain.enums.ResultStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollmentResponse {

    private Integer id;
    private Integer courseId;
    private String courseName;
    private ResultStatus result;
}
