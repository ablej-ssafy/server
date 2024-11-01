package me.noteme.headhunting.domain.resume.repository;

import me.noteme.headhunting.domain.resume.entity.Educational;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EducationalRepository extends JpaRepository<Educational, Long> {
    @Query("""
        SELECT e FROM Educational e
        JOIN e.resume r
        WHERE r.member.id = :memberId
        ORDER BY e.id ASC
    """)
    List<Educational> findAllByMemberId(@Param("memberId") Long memberId);
}
