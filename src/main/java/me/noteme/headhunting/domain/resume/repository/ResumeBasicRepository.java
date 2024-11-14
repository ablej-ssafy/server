package me.noteme.headhunting.domain.resume.repository;

import me.noteme.headhunting.domain.resume.entity.ResumeBasic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.Optional;

public interface ResumeBasicRepository extends JpaRepository<ResumeBasic, Long> {
    @Query("""
        SELECT rb
        FROM ResumeBasic rb
        JOIN Resume r
        ON rb.resume.id = r.id
        WHERE r.member.id = :memberId
    """)
    Optional<ResumeBasic> findByMemberId(@Param("memberId") Long memberId);

    @Query("""
        SELECT CASE WHEN COUNT(rb) > 0 THEN TRUE ELSE FALSE END
        FROM ResumeBasic rb
        WHERE rb.resume.id = :resumeId
    """)
    boolean existsByResumeId(@Param("resumeId") Long resumeId);

    @Modifying
    @Query("""
        DELETE FROM ResumeBasic rb
        WHERE rb.resume.member.id = :memberId
    """)
    void deleteAllByMemberId(@Param("memberId") Long memberId);
}
