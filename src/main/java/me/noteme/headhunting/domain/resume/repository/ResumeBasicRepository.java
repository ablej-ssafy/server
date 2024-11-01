package me.noteme.headhunting.domain.resume.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.noteme.headhunting.domain.resume.entity.ResumeBasic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResumeBasicRepository extends JpaRepository<ResumeBasic, Long> {
    @Query("""
        SELECT rb
        FROM ResumeBasic rb
        JOIN Resume r
        ON rb.resume.id = r.id
        WHERE r.member.id = :memberId
    """)
    ResumeBasic findByMemberId(@Param("memberId") Long memberId);
}
