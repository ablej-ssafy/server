package me.noteme.headhunting.domain.resume.repository;

import me.noteme.headhunting.domain.resume.entity.Certification;
import me.noteme.headhunting.domain.resume.entity.CertificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
                WHERE r.member.id = :memberId
                ORDER BY c.id ASC
            """)
    List<Certification> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("""
                SELECT c
                FROM Certification c
                JOIN Resume r
                ON r.id = c.resume.id
                WHERE r.member.id = :memberId
                AND c.certificationType = :type
                ORDER BY c.id ASC
            """)
    List<Certification> findAllByMemberIdAndType(@Param("memberId") Long memberId, @Param("type") CertificationType type);

    @Modifying
    @Query("""
                DELETE FROM Certification c
                WHERE c.resume.member.id = :memberId
            """)
    void deleteAllByMemberId(@Param("memberId") Long memberId);
}
