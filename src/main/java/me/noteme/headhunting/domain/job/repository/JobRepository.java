package me.noteme.headhunting.domain.job.repository;

import me.noteme.headhunting.domain.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT j FROM Job j " +
            "join InterestJob ij ON ij.job.id = j.id " +
            "WHERE ij.member.id = :userId")
    Optional<Job> findInterestJobById(@Param("userId") Long userId);
}
