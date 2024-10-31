package me.noteme.headhunting.domain.job.repository;

import me.noteme.headhunting.domain.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
