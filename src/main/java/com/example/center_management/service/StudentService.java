package com.example.center_management.service;

import java.util.List;

import com.example.center_management.dto.request.StudentCreateRequest;
import com.example.center_management.dto.request.StudentUpdateRequest;
import com.example.center_management.dto.response.StudentResponse;

public interface StudentService {

    StudentResponse create(StudentCreateRequest request);

    StudentResponse getById(Long id);

    List<StudentResponse> getAll();

    StudentResponse update(Long id, StudentUpdateRequest request);

    void delete(Long id);
    
}
