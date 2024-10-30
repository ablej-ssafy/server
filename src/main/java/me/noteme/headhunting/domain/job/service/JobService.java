package me.noteme.headhunting.domain.job.service;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.domain.job.dto.JobResponse;
import me.noteme.headhunting.domain.job.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobService {
    private final JobRepository jobRepository;

    public List<JobResponse> getJobs() {
        return jobRepository.findAll().stream()
                .map(job -> JobResponse.of(job.getId(), job.getJobTitle()))
                .toList();
    }
}
