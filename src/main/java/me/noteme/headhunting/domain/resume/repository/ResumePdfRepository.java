package me.noteme.headhunting.domain.resume.repository;

import me.noteme.headhunting.domain.resume.entity.ResumePdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResumePdfRepository extends JpaRepository<ResumePdf, Long> {
    List<ResumePdf> findAllByMemberId(@Param("memberId") Long memberId);
}
