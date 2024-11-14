package me.noteme.headhunting.domain.recruitment.repository;

import me.noteme.headhunting.domain.recruitment.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        LEFT JOIN FETCH r.category
        LEFT JOIN FETCH r.childCategories
        LEFT JOIN FETCH r.images
        LEFT JOIN FETCH r.company
        WHERE r.id IN :recruitmentIds
    """)
    List<Recruitment> findRecruitmentsById(@Param("recruitmentIds") List<Long> recruitmentIds);

    @Query("SELECT r.name FROM Recruitment r")
    List<String> findRecruitmentNames();
}
