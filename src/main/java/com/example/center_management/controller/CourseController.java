package com.example.center_management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;

import com.example.center_management.dto.request.CourseCreateRequest;
import com.example.center_management.dto.request.CourseUpdateRequest;
import com.example.center_management.dto.response.CourseResponse;
import com.example.center_management.service.CourseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
            @RequestParam(defaultValue = "5") int size
    ) {
        return courseService.getAll(page, size);
    }

    @PutMapping("/{id}")
    public CourseResponse update(
            @PathVariable Long id,
            @Valid @RequestBody CourseUpdateRequest request
    ) {
        return courseService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }
}
