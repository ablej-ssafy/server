package me.noteme.headhunting.domain.resume.repository;

import me.noteme.headhunting.domain.resume.entity.ReferenceUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferenceUrlRepository extends JpaRepository<ReferenceUrl, Long> {
}
