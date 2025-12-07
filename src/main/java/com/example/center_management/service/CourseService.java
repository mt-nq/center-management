package com.example.center_management.service;

import com.example.center_management.dto.request.ChapterCreateRequest;
import com.example.center_management.dto.request.ChapterUpdateRequest;
import com.example.center_management.dto.request.CourseCreateRequest;
import com.example.center_management.dto.request.CourseUpdateRequest;
import com.example.center_management.dto.request.LessonCreateRequest;
import com.example.center_management.dto.request.LessonUpdateRequest;
import com.example.center_management.dto.response.ChapterResponse;
import com.example.center_management.dto.response.CourseResponse;
import com.example.center_management.dto.response.CourseStructureResponse;
import com.example.center_management.dto.response.LessonResponse;
import org.springframework.data.domain.Page;

public interface CourseService {

    CourseResponse create(CourseCreateRequest request);

    CourseResponse getById(Long id);

    CourseResponse update(Long id, CourseUpdateRequest request);

    void delete(Long id);

    Page<CourseResponse> getAll(int page, int size);

    // ====== API mới theo bảng Course Management ======

    /**
     * Lấy chi tiết lộ trình (chapters -> lessons) của khóa học.
     */
    CourseStructureResponse getStructure(Long id);

    // Chapter
    ChapterResponse addChapter(Long courseId, ChapterCreateRequest request);
    ChapterResponse updateChapter(Long chapterId, ChapterUpdateRequest request);
    void deleteChapter(Long chapterId);

    // Lesson
    LessonResponse addLesson(Long chapterId, LessonCreateRequest request);
    LessonResponse updateLesson(Long lessonId, LessonUpdateRequest request);
    void deleteLesson(Long lessonId);

}
