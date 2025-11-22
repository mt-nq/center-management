package com.example.center_management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.center_management.dto.request.EnrollmentCreateRequest;
import com.example.center_management.dto.request.EnrollmentResultUpdateRequest;
import com.example.center_management.dto.response.CertificateResponse;
import com.example.center_management.dto.response.EnrollmentResponse;
import com.example.center_management.service.EnrollmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public EnrollmentResponse enroll(@Valid @RequestBody EnrollmentCreateRequest request) {
        return enrollmentService.enroll(request);
    }

@PutMapping("/{id}/certificate")
public CertificateResponse update(
        @PathVariable Long id,
        @RequestBody EnrollmentResultUpdateRequest request
) {
    return enrollmentService.updateResult(id, request);
}


    @GetMapping
    public List<EnrollmentResponse> getAll() {
        return enrollmentService.getAll();
    }

    @GetMapping("/by-student/{studentId}")
    public List<EnrollmentResponse> getByStudent(@PathVariable Long studentId) {
        return enrollmentService.getByStudent(studentId);
    }
}
