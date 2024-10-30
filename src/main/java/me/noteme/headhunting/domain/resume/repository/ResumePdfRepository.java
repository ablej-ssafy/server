package me.noteme.headhunting.domain.resume.repository;

import me.noteme.headhunting.domain.resume.entity.ResumePdf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumePdfRepository extends JpaRepository<ResumePdf, Long> {

}
