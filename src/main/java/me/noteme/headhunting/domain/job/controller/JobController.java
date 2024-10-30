package me.noteme.headhunting.domain.job.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.job.dto.JobResponse;
import me.noteme.headhunting.domain.job.service.JobService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jobs")
public class JobController {
    private final JobService jobService;

    @GetMapping
    public SuccessResponse<List<JobResponse>> getJobs() {
        return SuccessResponse.of(jobService.getJobs());
    }
}
