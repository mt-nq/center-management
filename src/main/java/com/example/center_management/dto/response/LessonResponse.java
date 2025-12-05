package com.example.center_management.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LessonResponse {
    private Long id;
    private String title;
    private String type;
    private String urlVid;
}
