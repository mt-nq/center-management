package com.example.center_management.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import com.example.center_management.dto.request.StudentUpdateRequest;
import com.example.center_management.dto.response.StudentResponse;
import com.example.center_management.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/students")
@RequiredArgsConstructor
public class AdminStudentController {

    private final StudentService studentService;

    // Admin xem chi tiết 1 student
    @GetMapping("/{id}")
    public StudentResponse getById(@PathVariable Long id) {
        return studentService.getById(id);
    }

    // Admin xem danh sách student (phân trang)
    @GetMapping
    public Page<StudentResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return studentService.getAll(page, size);
    }


    // Admin xoá (soft delete) student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
