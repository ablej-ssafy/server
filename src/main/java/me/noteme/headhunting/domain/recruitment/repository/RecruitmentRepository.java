package me.noteme.headhunting.domain.recruitment.repository;

import me.noteme.headhunting.domain.recruitment.entity.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long>, RecruitmentQueryRepository {
    @Query("""
        SELECT r
        FROM Recruitment r
        LEFT JOIN FETCH r.category c
        LEFT JOIN FETCH r.childCategories
        LEFT JOIN FETCH r.company co
        LEFT JOIN FETCH r.images i
        WHERE r.id = :recruitmentId
    """)
    Optional<Recruitment> findRecruitmentById(@Param("recruitmentId") Long recruitmentId);

    @Query("""
        SELECT r
        FROM Recruitment r
        JOIN FETCH r.company c
        JOIN FETCH r.category jc
        LEFT JOIN FETCH r.childCategories rc
        LEFT JOIN FETCH r.images i
        WHERE jc.id = :categoryId
    """)
    Page<Recruitment> findRecruitmentsByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
}
