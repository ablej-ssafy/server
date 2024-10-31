package me.noteme.headhunting.domain.resume.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.noteme.headhunting.domain.resume.entity.Certification;
import me.noteme.headhunting.domain.resume.entity.CertificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    @Query("""
        SELECT c
        FROM Certification c
        WHERE c.resume.id = :resumeId
        ORDER BY c.id ASC
    """)
    List<Certification> findAllByResumeId(@Param("resumeId") Long resumeId);

    @Query("""
        SELECT c
        FROM Certification c
        JOIN Resume r
        ON r.id = c.resume.id
        WHERE r.member.id = :userId
        ORDER BY c.id ASC
    """)
    List<Certification> findAllByMemberId(@Param("userId") Long userId);

    @Query("""
        SELECT c
        FROM Certification c
        JOIN Resume r
        ON r.id = c.resume.id
        WHERE r.member.id = :userId
        AND c.certificationType = :type
        ORDER BY c.id ASC
    """)
    List<Certification> findAllByMemberIdAndType(@Param("userId") Long userId, @Param("type") CertificationType type);
}
