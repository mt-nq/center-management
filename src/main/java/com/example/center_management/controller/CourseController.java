package com.example.center_management.controller;

import com.example.center_management.dto.request.CourseCreateRequest;
import com.example.center_management.dto.request.CourseUpdateRequest;
import com.example.center_management.dto.response.CourseResponse;
import com.example.center_management.service.CourseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public CourseResponse create(@Valid @RequestBody CourseCreateRequest request) {
        return courseService.create(request);
    }

    @GetMapping("/{id}")
    public CourseResponse getById(@PathVariable Long id) {
        return courseService.getById(id);
    }

    @GetMapping
    public Page<CourseResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status
    ) {
        return courseService.getAll(page, size, search, status);
    }

    @PutMapping("/{id}")
    public CourseResponse update(
            @PathVariable Long id,
            @Valid @RequestBody CourseUpdateRequest request
    ) {
        return courseService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }
}
