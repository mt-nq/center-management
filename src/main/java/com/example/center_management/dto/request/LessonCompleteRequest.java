package com.example.center_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonCompleteRequest {

    @NotNull
    private Long enrollmentId;
}
