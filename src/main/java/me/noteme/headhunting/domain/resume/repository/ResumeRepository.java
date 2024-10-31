package me.noteme.headhunting.domain.resume.repository;

import me.noteme.headhunting.domain.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
}
