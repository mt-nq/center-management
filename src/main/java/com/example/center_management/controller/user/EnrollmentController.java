package com.example.center_management.controller.user;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;

import com.example.center_management.dto.enrollment.EnrollmentCompletionResponse;
import com.example.center_management.dto.enrollment.EnrollmentResultUpdateRequest;
import com.example.center_management.dto.request.EnrollmentCreateRequest;
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

@GetMapping
    public Page<EnrollmentResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return enrollmentService.getAll(page, size);
    }

    @GetMapping("/all-enrollment/{studentId}")
    public List<EnrollmentResponse> getByStudent(@PathVariable Long studentId) {
        return enrollmentService.getByStudent(studentId);
    }

    @PutMapping("/admin/enrollments/{id}/result")
    public EnrollmentCompletionResponse updateCompletionResult(
            @PathVariable Long id,
            @RequestBody EnrollmentResultUpdateRequest request
    ) {
        return enrollmentService.updateCompletionResult(id, request.getResult());
    }



}
