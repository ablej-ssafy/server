package me.noteme.headhunting.domain.recruitment.repository;

import me.noteme.headhunting.domain.recruitment.entity.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface RecruitmentQueryRepository {
    Page<Recruitment> findRecruitmentsByCategoryId(Long categoryId, Pageable pageable);
    Page<Recruitment> findRecruitments(Pageable pageable);
    Page<Recruitment> searchRecruitments(String query, Pageable pageable);
}
