package com.example.center_management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.center_management.dto.request.CertificateCreateRequest;
import com.example.center_management.dto.response.CertificateResponse;
import com.example.center_management.service.CertificateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @PostMapping
    public CertificateResponse create(@RequestBody CertificateCreateRequest request) {
        return certificateService.create(request);
    }

    @GetMapping
    public List<CertificateResponse> getAll() {
        return certificateService.getAll();
    }

    @GetMapping("/{id}")
    public CertificateResponse getById(@PathVariable Long id) {
        return certificateService.getById(id);
    }

    @PutMapping("/revoke/{id}")
    public CertificateResponse revoke(@PathVariable Long id) {
        return certificateService.revoke(id);
    }
}

// ⭐ CẦN THÊM ENDPOINT CHO ENROLLMENT ĐÃ HOÀN THÀNH Ở ENROLLMENT CONTROLLER
// Giả định bạn có EnrollmentController:
/*
@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    // ...
    @GetMapping("/completed")
    public List<CompletedEnrollmentDTO> getCompletedEnrollments() {
        // Return danh sách Enrollment đã hoàn thành
        return enrollmentService.getCompletedEnrollments(); 
    }
}
*/