package com.example.center_management.service.impl;

import com.example.center_management.domain.entity.Enrollment;
import com.example.center_management.domain.entity.Lesson;
import com.example.center_management.domain.entity.LessonProgress;
import com.example.center_management.dto.response.EnrollmentProgressResponse;
import com.example.center_management.dto.response.LessonProgressResponse;
import com.example.center_management.exception.BadRequestException;
import com.example.center_management.exception.ResourceNotFoundException;
import com.example.center_management.repository.EnrollmentRepository;
import com.example.center_management.repository.LessonProgressRepository;
import com.example.center_management.repository.LessonRepository;
import com.example.center_management.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProgressServiceImpl implements ProgressService {

    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;
    private final LessonProgressRepository lessonProgressRepository;

    @Override
    @Transactional
    public LessonProgressResponse completeLesson(Long lessonId, Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        // check bài học có thuộc khóa học của enrollment không
        Long courseOfLesson = lesson.getChapter().getCourse().getId();
        Long courseOfEnrollment = enrollment.getCourse().getId();

        if (!courseOfLesson.equals(courseOfEnrollment)) {
            throw new BadRequestException("Lesson does not belong to enrollment course");
        }

        LessonProgress progress = lessonProgressRepository
                .findByEnrollment_IdAndLesson_Id(enrollmentId, lessonId)
                .orElseGet(() -> LessonProgress.builder()
                        .enrollment(enrollment)
                        .lesson(lesson)
                        .build()
                );

        progress.setCompleted(true);
        progress.setCompletedAt(LocalDateTime.now());

        lessonProgressRepository.save(progress);

        return LessonProgressResponse.builder()
                .enrollmentId(enrollmentId)
                .lessonId(lessonId)
                .completed(progress.isCompleted())
                .completedAt(progress.getCompletedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentProgressResponse getProgress(Long enrollmentId) {
        return buildProgressResponse(enrollmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentProgressResponse getProgressForAdmin(Long enrollmentId) {
        // hiện tại giống student, sau này muốn thêm info gì cho admin thì sửa tại đây
        return buildProgressResponse(enrollmentId);
    }

    private EnrollmentProgressResponse buildProgressResponse(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        Long courseId = enrollment.getCourse().getId();

        // tổng VIDEO trong khóa học
        long totalVideos = lessonRepository
                .countByChapter_Course_IdAndType(courseId, "VIDEO");

        // số VIDEO đã hoàn thành
        long completedVideos = lessonProgressRepository
                .countByEnrollment_IdAndCompletedTrueAndLesson_Type(enrollmentId, "VIDEO");

        double percent = 0.0;
        if (totalVideos > 0) {
            percent = (completedVideos * 100.0) / totalVideos;
        }

        return EnrollmentProgressResponse.builder()
                .enrollmentId(enrollment.getId())
                .studentId(enrollment.getStudent().getId())
                .courseId(courseId)
                .studentName(enrollment.getStudent().getFullName())
                .courseTitle(enrollment.getCourse().getTitle())
                .totalVideoLessons(totalVideos)
                .completedVideoLessons(completedVideos)
                .progressPercent(percent)
                .build();
        }
        @Override
        @Transactional(readOnly = true)
        public List<EnrollmentProgressResponse> getEnrollmentsWithFullProgress() {
        return enrollmentRepository.findAll()
                .stream()
                .map(enrollment -> buildProgressResponse(enrollment.getId())) // ✅ truyền Long
                .filter(resp -> {
                        Double p = resp.getProgressPercent();
                        return p != null && p >= 99.99; // coi ~100% là đủ điều kiện
                })
                .collect(Collectors.toList());
}

}
