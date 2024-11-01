package me.noteme.headhunting.domain.resume.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.noteme.headhunting.domain.resume.entity.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TechStackRepository extends JpaRepository<TechStack, Long> {
    @Query("""
        SELECT ts
        FROM TechStack ts
        JOIN FETCH ts.referenceUrls
        JOIN FETCH ts.stackSkills
        JOIN Resume r
        ON  ts.resume.id = r.id
        WHERE r.member.id = :memberId
    """)
    TechStack findByMemberId(@Param("memberId") Long memberId);
}
