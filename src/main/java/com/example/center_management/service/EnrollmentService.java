package com.example.center_management.service;

import java.util.List;

import com.example.center_management.dto.enrollment.EnrollmentCompletionResponse;
import com.example.center_management.dto.request.EnrollmentCreateRequest;
import com.example.center_management.dto.request.EnrollmentResultUpdateRequest;
import com.example.center_management.domain.enums.CompletionResult;
import com.example.center_management.dto.certificate.CertificateResponse;
import com.example.center_management.dto.response.EnrollmentResponse;
import com.example.center_management.dto.response.StudentResponse;
import org.springframework.data.domain.Page;

public interface EnrollmentService {

    EnrollmentResponse enroll(EnrollmentCreateRequest request);

    List<EnrollmentResponse> getByStudent(Long studentId);

    CertificateResponse updateResult(Long enrollmentId, EnrollmentResultUpdateRequest request);

    List<StudentResponse> getStudentsNotEnrolled();

    EnrollmentCompletionResponse checkCompletion(Long enrollmentId);

    // update kết quả đánh giá cuối (PASSED / FAILED)
    EnrollmentCompletionResponse updateCompletionResult(Long enrollmentId,CompletionResult result);


    Page<EnrollmentResponse> getAll(int page, int size);
}
