package me.noteme.headhunting.domain.member.feign;

import me.noteme.headhunting.domain.member.feign.request.RepoInfoRequest;
import me.noteme.headhunting.domain.member.feign.response.RepoAnalysisResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "GithubRequestClient", url = "${app.ai-gpu-url}")
public interface GithubRequestClient {
    @PostMapping("/analysis/repo/analyze")
    RepoAnalysisResponse repoAnalysis(@RequestBody RepoInfoRequest request);

    @GetMapping("/analysis/repo/status/{requestId}")
    RepoAnalysisResponse repoStatus(@PathVariable("requestId") String requestId);
}
