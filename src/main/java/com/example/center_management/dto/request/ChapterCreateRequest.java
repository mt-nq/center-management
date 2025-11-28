package com.example.center_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChapterCreateRequest {

    @NotBlank
    private String title;
}
