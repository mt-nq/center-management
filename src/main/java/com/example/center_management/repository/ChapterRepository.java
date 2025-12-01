package com.example.center_management.repository;

import com.example.center_management.domain.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

    List<Chapter> findByCourse_IdOrderBySortOrderAsc(Integer courseId);
}
