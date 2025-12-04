package com.example.center_management.controller.admin;

import com.example.center_management.dto.enrollment.EnrollmentResultUpdateRequest;
import com.example.center_management.dto.response.EnrollmentProgressResponse;
import com.example.center_management.domain.enums.CompletionResult;
import com.example.center_management.dto.enrollment.EnrollmentCompletionResponse;
import com.example.center_management.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.center_management.service.ProgressService;
import java.util.List;

@RestController
@RequestMapping("/api/admin/enrollments")
@RequiredArgsConstructor
public class AdminEnrollmentController {

    private final EnrollmentService enrollmentService;
    private final ProgressService progressService;

    // PUT /api/admin/enrollments/{id}/result
    @PutMapping("/{id}/result")
    public EnrollmentCompletionResponse updateCompletionResult(
            @PathVariable("id") Long enrollmentId,
            @RequestBody EnrollmentResultUpdateRequest request
    ) {
        CompletionResult result = request.getResult();
        return enrollmentService.updateCompletionResult(enrollmentId, result);
    }

    @GetMapping("/ready-for-certificate")
    public List<EnrollmentProgressResponse> getReadyForCertificate() {
        return progressService.getEnrollmentsWithFullProgress();
    }
}
