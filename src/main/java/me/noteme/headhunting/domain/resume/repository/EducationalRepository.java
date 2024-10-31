package me.noteme.headhunting.domain.resume.repository;

import me.noteme.headhunting.domain.resume.entity.Educational;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationalRepository extends JpaRepository<Educational, Long> {
}
