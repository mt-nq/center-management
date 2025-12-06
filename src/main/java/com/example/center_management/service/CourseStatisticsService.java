package com.example.center_management.service;

import com.example.center_management.dto.response.CourseYearStatisticResponse;

import java.util.List;

public interface CourseStatisticsService {

    List<CourseYearStatisticResponse> getCourseStatisticsByYear(int year);
}
