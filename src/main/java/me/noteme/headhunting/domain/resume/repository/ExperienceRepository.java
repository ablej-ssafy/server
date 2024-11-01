package me.noteme.headhunting.domain.resume.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.noteme.headhunting.domain.resume.entity.Experience;
import me.noteme.headhunting.domain.resume.entity.ExperienceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    @Query("""
        SELECT e
        FROM Experience e
        JOIN Resume r
        ON e.resume.id = r.id
        WHERE r.member.id = :memberId
        ORDER BY e.id ASC
    """)
    List<Experience> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("""
        SELECT e
        FROM Experience e
        JOIN Resume r
        ON e.resume.id = r.id
        WHERE r.member.id = :memberId
        AND e.experienceType = :type
        ORDER BY e.id ASC
    """)
    List<Experience> findAllByMemberIdAndType(@Param("memberId") Long memberId, @Param("type") ExperienceType type);
}
