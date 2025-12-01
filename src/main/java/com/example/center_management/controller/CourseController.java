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

    // GET /courses – Lấy danh sách tất cả khóa học (Public/Student) – 200
    @GetMapping
    public Page<CourseResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return courseService.getAll(page, size);
    }

    // (Bonus) GET /courses/{id} – chi tiết course, không có trong bảng nhưng không sao
    @GetMapping("/{id}")
    public CourseResponse getById(@PathVariable Long id) {
        return courseService.getById(id);
    }

    // GET /courses/{id}/structure – Lấy chi tiết lộ trình (chapters -> lessons) – 200
    @GetMapping("/{id}/structure")
    public CourseStructureResponse getStructure(@PathVariable Long id) {
        return courseService.getStructure(id);
    }
}
