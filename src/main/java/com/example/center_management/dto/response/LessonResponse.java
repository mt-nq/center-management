    package com.example.center_management.dto.response;

    import com.example.center_management.domain.entity.Lesson;

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

        public static LessonResponse fromEntity(Lesson lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .type(lesson.getType())
                .urlVid(lesson.getUrlVid())
                .build();
    }
    }
