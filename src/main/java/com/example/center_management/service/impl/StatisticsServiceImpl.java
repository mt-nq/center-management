package com.example.center_management.service.impl;

import com.example.center_management.repository.StudentRepository;
import com.example.center_management.repository.StudentRepository.LocationStat;
import com.example.center_management.service.StatisticsService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StudentRepository studentRepository;

    @Override
    public List<LocationStat> getStudentStatsByHometown() {
        return studentRepository.countByHometown();
    }

    @Override
    public List<LocationStat> getStudentStatsByProvince() {
        return studentRepository.countByProvince();
    }
}
