package me.noteme.headhunting.domain.recruitment.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.recruitment.controller.request.CompanyAnalyzeRequest;
import me.noteme.headhunting.domain.recruitment.controller.request.ResumeKeywordsRequest;
import me.noteme.headhunting.domain.recruitment.feign.response.RecommendResponse;
import me.noteme.headhunting.domain.recruitment.service.RecruitmentService;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruitment")
public class RecruitmentController {
    private final RecruitmentService recruitmentService;

    @PostMapping("/recommend")
    public SuccessResponse<List<RecommendResponse>> analyzeResume(
            @LoginUser Long userId,
            MultipartFile file
    ) {
        return SuccessResponse.of(recruitmentService.analyzeResume(userId, file));
    }


    @Deprecated
    @PostMapping("/resume/keywords")
    public SuccessResponse<List<String>> getResumeKeywords(
            @Validated @RequestBody ResumeKeywordsRequest request,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST, errors);
        }

        return SuccessResponse.of(
                recruitmentService.getResumeKeywords(request.getJobId(), request.getJobSubId(), request.getResume())
        );
    }

    @Deprecated
    @PostMapping("/company/analyze")
    public SuccessResponse<String> getCompanyAnalyze(
            @Validated @RequestBody CompanyAnalyzeRequest request,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST, errors);
        }

        return SuccessResponse.of(recruitmentService.getCompanyAnalyze(request.getCompanyName()));
    }
}

