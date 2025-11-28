package com.example.center_management.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.example.center_management.dto.response.CourseResponse;
import com.example.center_management.dto.response.CourseStructureResponse;
import com.example.center_management.service.CourseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // Lấy danh sách khóa học (Public/Student) – có phân trang
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
