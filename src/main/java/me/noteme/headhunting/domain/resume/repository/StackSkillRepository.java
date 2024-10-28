package me.noteme.headhunting.domain.resume.repository;

import me.noteme.headhunting.domain.resume.entity.StackSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StackSkillRepository extends JpaRepository<StackSkill, Long> {
}
