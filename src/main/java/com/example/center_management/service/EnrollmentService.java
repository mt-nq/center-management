package com.example.center_management.service;

import java.util.List;

import com.example.center_management.dto.request.EnrollmentCreateRequest;
import com.example.center_management.dto.request.EnrollmentResultUpdateRequest;
import com.example.center_management.dto.response.CertificateResponse;
import com.example.center_management.dto.response.EnrollmentResponse;

public interface EnrollmentService {

    EnrollmentResponse enroll(EnrollmentCreateRequest request);

    List<EnrollmentResponse> getAll();

    List<EnrollmentResponse> getByStudent(Long studentId);

    CertificateResponse updateResult(Long enrollmentId, EnrollmentResultUpdateRequest request);
}
