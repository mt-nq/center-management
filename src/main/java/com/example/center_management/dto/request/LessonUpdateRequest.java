package com.example.center_management.dto.request;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class LessonUpdateRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String type;

    private String urlVid;

}