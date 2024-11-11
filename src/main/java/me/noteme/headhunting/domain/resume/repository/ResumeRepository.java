package me.noteme.headhunting.domain.resume.repository;

import me.noteme.headhunting.domain.resume.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    @Query("""
        SELECT r
        FROM Resume r
        WHERE r.member.id = :memberId
    """)
    Optional<Resume> findByMemberId(@Param("memberId") Long memberId);

    @Query("""
        SELECT e
        FROM Resume r
        JOIN Education e
        ON e.resume.id = r.id
        WHERE r.member.id = :memberId
        ORDER BY e.id ASC
    """)
    List<Education> findAllEducationByMemberId(@Param("memberId") Long memberId);

    @Query("""
        SELECT e
        FROM Resume r
        JOIN Experience e
        ON e.resume.id = r.id
        WHERE r.member.id = :memberId
        ORDER BY e.id ASC
    """)
    List<Experience> findAllExperienceByMemberId(@Param("memberId") Long memberId);

    @Query("""
        SELECT c
        FROM Resume r
        JOIN Certification c
        ON c.resume.id = r.id
        WHERE r.member.id = :memberId
        ORDER BY c.id ASC
    """)
    List<Certification> findAllCertificationsByMemberId(@Param("memberId") Long memberId);

    @Query("""
        SELECT t
        FROM Resume r
        JOIN TechStack t
        ON t.resume.id = r.id
        WHERE r.member.id = :memberId
        ORDER BY t.id ASC
    """)
    Optional<TechStack> findTechByMemberId(@Param("memberId") Long memberId);
}
