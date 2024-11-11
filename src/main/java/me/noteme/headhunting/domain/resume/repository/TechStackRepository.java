package me.noteme.headhunting.domain.resume.repository;

import me.noteme.headhunting.domain.resume.entity.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TechStackRepository extends JpaRepository<TechStack, Long> {
    @Query("""
        SELECT ts
        FROM TechStack ts
        JOIN FETCH ts.stackSkills
        JOIN Resume r
        ON  ts.resume.id = r.id
        WHERE r.member.id = :memberId
    """)
    Optional<TechStack> findByMemberId(@Param("memberId") Long memberId);
}
