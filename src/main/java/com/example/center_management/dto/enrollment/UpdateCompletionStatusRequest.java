package com.example.center_management.dto.enrollment;

import lombok.Data;

@Data
public class UpdateCompletionStatusRequest {
    /**
     * true  -> COMPLETED
     * false -> NOT_COMPLETED
     */
    private boolean completed;
}
