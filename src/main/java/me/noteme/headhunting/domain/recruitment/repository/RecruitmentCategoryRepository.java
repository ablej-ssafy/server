package me.noteme.headhunting.domain.recruitment.repository;

import me.noteme.headhunting.domain.recruitment.entity.RecruitmentCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecruitmentCategoryRepository extends JpaRepository<RecruitmentCategory, Long> {

    @Query("""
        SELECT DISTINCT r.recruitment.id
        FROM RecruitmentCategory r
        WHERE r.category.id IN :categoryIds
    """)
    Page<Long> findRecruitmentIdsByCategoryIds(@Param("categoryIds") List<Long> jobCategoryIds, Pageable pageable);
}
