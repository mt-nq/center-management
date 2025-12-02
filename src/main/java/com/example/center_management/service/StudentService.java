package com.example.center_management.service;

import java.util.List;

import com.example.center_management.dto.auth.StudentRegisterRequest;
import com.example.center_management.dto.request.StudentUpdateRequest;
import com.example.center_management.dto.response.StudentResponse;
import org.springframework.data.domain.Page;
import com.example.center_management.domain.entity.User;


public interface StudentService {

    StudentResponse createForUser(User user, StudentRegisterRequest request);

    StudentResponse getById(Long id);

    Page<StudentResponse> getAll(int page, int size);

    StudentResponse update(Long id, StudentUpdateRequest request);

    void delete(Long id);

}

