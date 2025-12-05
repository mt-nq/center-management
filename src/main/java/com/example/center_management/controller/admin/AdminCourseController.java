package com.example.center_management.controller.admin;

import com.example.center_management.dto.request.ChapterCreateRequest;
import com.example.center_management.dto.request.CourseCreateRequest;
import com.example.center_management.dto.request.CourseUpdateRequest;
import com.example.center_management.dto.response.ChapterResponse;
import com.example.center_management.dto.response.CourseResponse;
import com.example.center_management.dto.response.CourseStructureResponse;
import com.example.center_management.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

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
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
    courseService.delete(id); // soft delete: set INACTIVE
}

    @GetMapping
    public Page<CourseResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return courseService.getAll(page, size);
    }

    // Lấy chi tiết 1 khóa học theo id
    @GetMapping("/{id}")
    public CourseResponse getById(@PathVariable Long id) {
        return courseService.getById(id);
    }

    // Lấy chi tiết lộ trình (chapters -> lessons) của khóa học
    @GetMapping("/{id}/structure")
    public CourseStructureResponse getStructure(@PathVariable Long id) {
        return courseService.getStructure(id);
    }

}
