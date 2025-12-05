package com.example.center_management.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LessonProgressResponse {

    private Long enrollmentId;

    private Long lessonId;
    private String lessonName;
    private Integer lessonOrder;

    private boolean completed;
    private LocalDateTime completedAt;
}
