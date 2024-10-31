package me.noteme.headhunting.domain.resume.repository;

import me.noteme.headhunting.domain.resume.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
}
