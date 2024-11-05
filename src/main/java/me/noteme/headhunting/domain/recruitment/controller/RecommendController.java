package me.noteme.headhunting.domain.recruitment.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.recruitment.controller.request.CompanyAnalyzeRequest;
import me.noteme.headhunting.domain.recruitment.controller.request.ResumeKeywordsRequest;
import me.noteme.headhunting.domain.recruitment.feign.response.RecommendResponse;
import me.noteme.headhunting.domain.recruitment.service.RecommendService;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommend")
public class RecommendController {
    private final RecommendService recommendService;

    @PostMapping
    public SuccessResponse<List<RecommendResponse>> analyzeResume(
            @LoginUser Long memberId,
            @RequestPart("file") MultipartFile file
    ) {
        return SuccessResponse.of(recommendService.analyzeResume(memberId, file));
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
                recommendService.getResumeKeywords(request.getJobId(), request.getJobSubId(), request.getResume())
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

        return SuccessResponse.of(recommendService.getCompanyAnalyze(request.getCompanyName()));
    }
}

