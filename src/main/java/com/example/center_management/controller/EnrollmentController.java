package com.example.center_management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.center_management.dto.enrollment.EnrollmentCompletionResponse;
import com.example.center_management.dto.enrollment.UpdateCompletionStatusRequest;
import com.example.center_management.dto.request.EnrollmentCreateRequest;
import com.example.center_management.dto.request.EnrollmentResultUpdateRequest;
import com.example.center_management.dto.certificate.CertificateResponse;
import com.example.center_management.dto.response.EnrollmentResponse;
import com.example.center_management.dto.response.StudentResponse;
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
    @GetMapping("/unregistered-students")
    public List<StudentResponse> getUnregisteredStudents() {
        return enrollmentService.getStudentsNotEnrolled();
    }

     // API xem học viên đã hoàn thành khóa chưa
    @GetMapping("/{id}/completion-status")
    public EnrollmentCompletionResponse getCompletionStatus(@PathVariable("id") Long enrollmentId) {
        return enrollmentService.checkCompletion(enrollmentId);
    }

    // API cập nhật trạng thái hoàn thành / không hoàn thành
    @PutMapping("/{id}/completion-status")
    public void updateCompletionStatus(
            @PathVariable("id") Long enrollmentId,
            @RequestBody UpdateCompletionStatusRequest request
    ) {
        enrollmentService.updateCompletionStatus(enrollmentId, request.isCompleted());
    }
}
