package me.noteme.headhunting.domain.resume.repository;

import me.noteme.headhunting.domain.resume.entity.ResumeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ResumeOrderRepository extends JpaRepository<ResumeOrder, Long> {
    @Query("""
            SELECT ro
            FROM ResumeOrder ro
            JOIN FETCH ro.resume
            JOIN FETCH ro.resume.member
            WHERE ro.resume.id = :resumeId
    """)
    Optional<ResumeOrder> findByResumeId(@Param("resumeId") Long resumeId);
}
