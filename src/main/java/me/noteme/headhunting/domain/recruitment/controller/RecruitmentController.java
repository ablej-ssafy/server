package me.noteme.headhunting.domain.recruitment.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.response.BaseResponse;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.recruitment.controller.request.ResumeKeywordsRequest;
import me.noteme.headhunting.domain.recruitment.service.RecruitmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruitment")
@RequiredArgsConstructor
public class RecruitmentController {
    private final RecruitmentService recruitmentService;

    @PostMapping("/resume/keywords")
    public SuccessResponse<List<String>> getResumeKeywords(@RequestBody ResumeKeywordsRequest request) {
        return SuccessResponse.of(
                recruitmentService
                        .getResumeKeywords(
                                request.getJobId(),
                                request.getJobSubId(),
                                request.getResume()
                        )
        );
    }

    @PostMapping("/company/analyze")
    public SuccessResponse<String> getCompanyAnalyze(@RequestBody String companyName) {
        return SuccessResponse.of(
                recruitmentService
                        .getCompanyAnalyze(companyName)
        );
    }

}
