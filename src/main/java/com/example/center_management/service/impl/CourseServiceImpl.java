package com.example.center_management.service.impl;

import java.time.LocalDate;
import java.util.List;

import com.example.center_management.domain.entity.Chapter;
import com.example.center_management.domain.entity.Course;
import com.example.center_management.domain.entity.Lesson;
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
import com.example.center_management.exception.BadRequestException;
import com.example.center_management.exception.ResourceNotFoundException;
import com.example.center_management.repository.ChapterRepository;
import com.example.center_management.repository.CourseRepository;
import com.example.center_management.repository.LessonProgressRepository;
import com.example.center_management.repository.LessonRepository;
import com.example.center_management.service.CourseService;
import com.example.center_management.domain.enums.CourseStatus;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;
    private final LessonRepository lessonRepository;

    @Autowired
    private LessonProgressRepository lessonProgressRepository;

    // ============================================================
    // CRUD cơ bản cho Course
    // ============================================================

    @Override
    @Transactional
    public CourseResponse create(CourseCreateRequest request) {
        validateDates(request.getStartDate(), request.getEndDate());

        Course course = Course.builder()
                .title(request.getTitle())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .content(request.getContent())
                .status(CourseStatus.ACTIVE)
                .price(request.getPrice())
                .build();

        course.setCode(generateCourseCode());

        Course saved = courseRepository.save(course);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return toResponse(course);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseResponse> getAll(int page, int size) {

        List<CourseResponse> allActiveCourses = courseRepository.findAll()
                .stream()
                .filter(s -> s.getStatus() == CourseStatus.ACTIVE)
                .map(this::toResponse)
                .toList();

        int total = allActiveCourses.size();
        int fromIndex = page * size;

        if (fromIndex >= total) {
            return new PageImpl<>(
                    List.of(),
                    PageRequest.of(page, size),
                    total
            );
        }

        int toIndex = Math.min(fromIndex + size, total);
        List<CourseResponse> pageContent = allActiveCourses.subList(fromIndex, toIndex);

        return new PageImpl<>(
                pageContent,
                PageRequest.of(page, size),
                total
        );
    }

    @Override
    @Transactional
    public CourseResponse update(Long id, CourseUpdateRequest request) {
        validateDates(request.getStartDate(), request.getEndDate());

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        course.setTitle(request.getTitle());
        course.setStartDate(request.getStartDate());
        course.setEndDate(request.getEndDate());
        course.setContent(request.getContent());
        course.setPrice(request.getPrice()); 

        Course updated = courseRepository.saveAndFlush(course);

        return toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long courseId) {
        // 1. Lấy course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        // 2. Soft delete tất cả chapter và lesson con
        List<Chapter> chapters = chapterRepository.findByCourseId(courseId);
        for (Chapter chapter : chapters) {
            List<Lesson> lessons = lessonRepository.findByChapterId(chapter.getId());
            for (Lesson lesson : lessons) {
                lesson.setStatus(CourseStatus.INACTIVE); // Soft delete lesson
                lessonRepository.save(lesson);
            }
            chapter.setStatus(CourseStatus.INACTIVE); // Soft delete chapter
            chapterRepository.save(chapter);
        }

        // 3. Soft delete course
        course.setStatus(CourseStatus.INACTIVE);
        courseRepository.save(course);
    }
    // ============================================================
    // Chapter
    // ============================================================

    @Override
    @Transactional
    public ChapterResponse addChapter(Long courseId, ChapterCreateRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Chapter chapter = Chapter.builder()
                .title(request.getTitle())
                .course(course)
                .build();

        chapterRepository.save(chapter);
        return toChapterResponse(chapter);
    }

    @Override
    @Transactional
    public ChapterResponse updateChapter(Long chapterId, ChapterUpdateRequest request) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));

        chapter.setTitle(request.getTitle());

        chapterRepository.save(chapter);
        return toChapterResponse(chapter);
    }

    @Override
    @Transactional
    public void deleteChapter(Long chapterId) {
        // 1. Lấy tất cả lesson trong chapter
        List<Lesson> lessons = lessonRepository.findByChapterId(chapterId);

        // 2. Xóa các lesson_progress liên quan từng lesson
        for (Lesson lesson : lessons) {
            lessonProgressRepository.deleteByLessonId(lesson.getId());
        }

        // 3. Xóa các lesson
        lessonRepository.deleteAll(lessons);

        // 4. Xóa chapter
        chapterRepository.deleteById(chapterId);
    }

    // ============================================================
    // Lesson
    // ============================================================

    @Override
    @Transactional
    public LessonResponse addLesson(Long chapterId, LessonCreateRequest request) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));

        Lesson lesson = Lesson.builder()
                .title(request.getTitle())
                .type(request.getType())
                .chapter(chapter)
                .urlVid(request.getUrlVid())
                .build();

        lessonRepository.save(lesson);
        return toLessonResponse(lesson);
    }

    @Override
    @Transactional
    public LessonResponse updateLesson(Long lessonId, LessonUpdateRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        lesson.setTitle(request.getTitle());
        lesson.setType(request.getType());
        lesson.setUrlVid(request.getUrlVid());

        lessonRepository.save(lesson);
        return toLessonResponse(lesson);
    }

    @Override
    @Transactional
    public void deleteLesson(Long lessonId) {
        // 1. Xóa tất cả lesson_progress liên quan
        lessonProgressRepository.deleteByLessonId(lessonId);

        // 2. Xóa lesson
        lessonRepository.deleteById(lessonId);
    }

    // ============================================================
    // API helper methods
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public CourseStructureResponse getStructure(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        return toCourseStructure(course);
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (start == null || end == null) return;

        if (end.isBefore(start)) {
            throw new BadRequestException("End date must be after start date");
        }
    }

    private String generateCourseCode() {
        long count = courseRepository.count() + 1;
        return String.format("C-%04d", count);
    }

    private CourseResponse toResponse(Course c) {
        CourseResponse res = new CourseResponse();
        res.setId(c.getId());
        res.setCode(c.getCode());
        res.setTitle(c.getTitle());
        res.setStartDate(c.getStartDate());
        res.setEndDate(c.getEndDate());
        res.setContent(c.getContent());
        res.setStatus(c.getStatus().name());
        res.setPrice(c.getPrice());
        res.setCreatedAt(c.getCreatedAt());
        res.setUpdatedAt(c.getUpdatedAt());
        return res;
    }

    private CourseStructureResponse toCourseStructure(Course course) {
        return CourseStructureResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getContent())
                .chapters(
                        course.getChapters().stream()
                                .map(this::toChapterResponse)
                                .toList()
                )
                .build();
    }

    private ChapterResponse toChapterResponse(Chapter chapter) {
        return ChapterResponse.builder()
                .id(chapter.getId())
                .title(chapter.getTitle())
                .lessons(
                        chapter.getLessons().stream()
                                .map(this::toLessonResponse)
                                .toList()
                )
                .build();
    }

    private LessonResponse toLessonResponse(Lesson lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .type(lesson.getType())
                .urlVid(lesson.getUrlVid())
                .build();
    }
}