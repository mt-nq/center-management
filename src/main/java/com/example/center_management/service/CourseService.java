package com.example.center_management.service;

import java.util.List;

import com.example.center_management.dto.request.CourseCreateRequest;
import com.example.center_management.dto.request.CourseUpdateRequest;
import com.example.center_management.dto.response.CourseResponse;

public interface CourseService {

    CourseResponse create(CourseCreateRequest request);

    CourseResponse getById(Long id);

    List<CourseResponse> getAll();

    CourseResponse update(Long id, CourseUpdateRequest request);

    void delete(Long id);
}
