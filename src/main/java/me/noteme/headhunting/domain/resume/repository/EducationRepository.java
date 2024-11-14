package me.noteme.headhunting.domain.resume.repository;

import me.noteme.headhunting.domain.resume.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Long> {
    @Query("""
        SELECT e FROM Education e
        JOIN e.resume r
        WHERE r.member.id = :memberId
        ORDER BY e.id ASC
    """)
    List<Education> findAllByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("""
        DELETE FROM Education e
        WHERE e.resume.member.id = :memberId
    """)
    void deleteAllByMemberId(@Param("memberId") Long memberId);
}
