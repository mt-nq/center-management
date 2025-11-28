package com.example.center_management.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CourseStructureResponse {

    private Long id;
    private String title;
    private String description;

    // chapters -> lessons
    private List<ChapterResponse> chapters;
}
