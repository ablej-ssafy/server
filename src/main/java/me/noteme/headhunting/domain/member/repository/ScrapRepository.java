package me.noteme.headhunting.domain.member.repository;

import me.noteme.headhunting.domain.member.entity.Scrap;
import me.noteme.headhunting.domain.recruitment.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    @Query("""
        SELECT s.recruitment
        FROM Scrap s
        WHERE s.member.id = :memberId
    """)
    List<Recruitment> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("""
        SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END
        FROM Scrap s
        WHERE s.member.id = :memberId AND s.recruitment.id = :recruitmentId
    """)
    boolean isScrapped(@Param("memberId") Long memberId,@Param("recruitmentId") Long recruitmentId);

    @Query("""
        SELECT s
        FROM Scrap s
        WHERE s.member.id = :memberId AND s.recruitment.id = :recruitmentId
    """)
    Optional<Scrap> findScrap(@Param("memberId") Long memberId, @Param("recruitmentId") Long recruitmentId);

    @Query("""
        SELECT s.recruitment.id
        FROM Scrap s
        WHERE s.member.id = :memberId AND s.recruitment.id IN :recruitmentIds
    """)
    Set<Long> isScrapped(@Param("memberId") Long memberId, @Param("recruitmentIds") List<Long> recruitmentIds);
}
