package me.noteme.headhunting.domain.recruitment.repository;

import me.noteme.headhunting.domain.recruitment.entity.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecruitmentQueryRepository {
    Page<Recruitment> findRecruitmentsByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    Page<Recruitment> searchRecruitments(@Param("query") String query, Pageable pageable);
}
