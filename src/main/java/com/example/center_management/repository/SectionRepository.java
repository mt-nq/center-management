package com.example.center_management.repository;

import com.example.center_management.domain.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Integer> {

    List<Section> findByChapter_IdOrderBySortOrderAsc(Integer chapterId);
}
