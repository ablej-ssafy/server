package me.noteme.headhunting.domain.resume.repository;

import me.noteme.headhunting.domain.resume.entity.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechStackRepository extends JpaRepository<TechStack, Long> {
}
