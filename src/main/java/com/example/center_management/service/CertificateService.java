package com.example.center_management.service;

import java.util.List;

import com.example.center_management.dto.request.CertificateCreateRequest;
import com.example.center_management.dto.response.CertificateResponse;

public interface CertificateService {

    CertificateResponse create(CertificateCreateRequest request);

    List<CertificateResponse> getAll();

    CertificateResponse getById(Long id);

    CertificateResponse revoke(Long id);
}
