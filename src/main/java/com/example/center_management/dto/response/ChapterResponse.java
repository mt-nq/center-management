    package com.example.center_management.dto.response;

    import lombok.Builder;
    import lombok.Getter;
    import lombok.Setter;

    import java.util.List;

    import com.example.center_management.domain.entity.Chapter;

    @Getter
    @Setter
    @Builder
    public class ChapterResponse {
    private Long id;
    private String title;
    private List<LessonResponse> lessons;
        public static ChapterResponse fromEntity(Chapter chapter) {
        return ChapterResponse.builder()
                .id(chapter.getId())
                .title(chapter.getTitle())
                .lessons(chapter.getLessons() != null
                        ? chapter.getLessons().stream()
                                    .map(LessonResponse::fromEntity)
                                    .toList()
                        : List.of())
                .build();
    }
    }
