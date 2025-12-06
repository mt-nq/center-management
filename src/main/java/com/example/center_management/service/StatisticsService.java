package com.example.center_management.service;

import com.example.center_management.repository.StudentRepository.LocationStat;

import java.util.List;

public interface StatisticsService {

    List<LocationStat> getStudentStatsByHometown();

    List<LocationStat> getStudentStatsByProvince();
}
