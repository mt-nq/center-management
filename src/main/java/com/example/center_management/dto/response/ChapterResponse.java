package com.example.center_management.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ChapterResponse {
    private Long id;
    private String title;
    private List<LessonResponse> lessons;
}
