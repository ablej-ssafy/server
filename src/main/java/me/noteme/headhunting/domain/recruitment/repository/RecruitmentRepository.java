package me.noteme.headhunting.domain.recruitment.repository;

import me.noteme.headhunting.domain.recruitment.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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
}
