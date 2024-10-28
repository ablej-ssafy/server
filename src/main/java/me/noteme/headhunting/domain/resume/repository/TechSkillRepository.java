package me.noteme.headhunting.domain.resume.repository;

import me.noteme.headhunting.domain.resume.entity.TechSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechSkillRepository extends JpaRepository<TechSkill, Long> {
}
