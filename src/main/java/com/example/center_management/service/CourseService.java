package com.example.center_management.service;

import com.example.center_management.dto.request.CourseCreateRequest;
import com.example.center_management.dto.request.CourseUpdateRequest;
import com.example.center_management.dto.response.CourseResponse;
import org.springframework.data.domain.Page;

public interface CourseService {

    CourseResponse create(CourseCreateRequest request);

    CourseResponse getById(Long id);

    CourseResponse update(Long id, CourseUpdateRequest request);

    void delete(Long id);

    Page<CourseResponse> getAll(int page, int size, String search, String status);
}
