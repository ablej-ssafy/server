package me.noteme.headhunting.domain.resume.repository;

import me.noteme.headhunting.domain.resume.entity.ResumeBasic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeBasicRepository extends JpaRepository<ResumeBasic, Long> {
}
