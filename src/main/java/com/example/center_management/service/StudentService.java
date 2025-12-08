package com.example.center_management.service;

import java.util.List;

import com.example.center_management.dto.auth.AuthRegisterRequest;
import com.example.center_management.dto.request.StudentUpdateRequest;
import com.example.center_management.dto.response.StudentResponse;
import org.springframework.data.domain.Page;

import com.example.center_management.domain.entity.Student;
import com.example.center_management.domain.entity.User;
import java.util.Optional;

public interface StudentService {

    Student createForUser(User user, AuthRegisterRequest request);

    StudentResponse getById(Long id);

    Page<StudentResponse> getAll(int page, int size);

    StudentResponse update(Long id, StudentUpdateRequest request);

    void delete(Long id);

    Optional<Student> findByUserId(Long userId);

    Long findStudentIdByUsername(String username);

}

