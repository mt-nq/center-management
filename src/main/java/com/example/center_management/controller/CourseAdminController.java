package com.example.center_management.controller;

import com.example.center_management.dto.request.ChapterCreateRequest;
import com.example.center_management.dto.request.CourseCreateRequest;
import com.example.center_management.dto.request.CourseUpdateRequest;
import com.example.center_management.dto.response.ChapterResponse;
import com.example.center_management.dto.response.CourseResponse;
import com.example.center_management.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/courses")
@RequiredArgsConstructor
public class CourseAdminController {

    private final CourseService courseService;

    @PostMapping
    public CourseResponse create(@Valid @RequestBody CourseCreateRequest request) {
        return courseService.create(request);
    }

    @PutMapping("/{id}")
    public CourseResponse update(@PathVariable Long id,
                                 @Valid @RequestBody CourseUpdateRequest request) {
        return courseService.update(id, request);
    }

    @PostMapping("/{courseId}/chapters")
    public ChapterResponse addChapter(@PathVariable Long courseId,
                                      @Valid @RequestBody ChapterCreateRequest request) {
        return courseService.addChapter(courseId, request);
    }
}
