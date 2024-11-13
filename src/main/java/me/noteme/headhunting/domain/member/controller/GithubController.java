package me.noteme.headhunting.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.member.controller.request.GitAnalysisResultRequest;
import me.noteme.headhunting.domain.member.feign.request.RepoInfoRequest;
import me.noteme.headhunting.domain.member.feign.response.RepoAnalysisResponse;
import me.noteme.headhunting.domain.member.service.GithubService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/github/analysis")
@RequiredArgsConstructor
public class GithubController {
    private final GithubService githubService;

    @PostMapping("")
    public SuccessResponse<RepoAnalysisResponse> gitAnalysis(
            @LoginUser Long memberId,
            @Validated @RequestBody RepoInfoRequest request
    ) {
        return SuccessResponse.of(
                githubService.repoAnalysis(memberId, request.getOwner(), request.getRepo(), request.getBranch(), request.getToken())
        );
    }

    @GetMapping("/{requestId}")
    public SuccessResponse<RepoAnalysisResponse> getAnalysisInfo(@PathVariable("requestId") String requestId) {
        return SuccessResponse.of(
                githubService.getRepoAnalysisStatus(requestId)
        );
    }

    @PostMapping("/result")
    public SuccessResponse<Void> sendAnalysisResult(@Validated @RequestBody GitAnalysisResultRequest request) {
        githubService.sendAnalysisResult(request.getEmail(), request.getRepositoryName(), request.getAnalysisSummary());
        return SuccessResponse.empty();
    }
}
