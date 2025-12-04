// UpdateCompletionStatusRequest.java
package com.example.center_management.dto.enrollment;

import com.example.center_management.domain.enums.CompletionResult;
import lombok.Data;

@Data
public class EnrollmentResultUpdateRequest {
    private CompletionResult result;
}
