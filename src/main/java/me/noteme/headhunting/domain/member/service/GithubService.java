package me.noteme.headhunting.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.utils.KeyUtils;
import me.noteme.headhunting.domain.member.feign.GithubRequestClient;
import me.noteme.headhunting.domain.member.feign.request.RepoInfoRequest;
import me.noteme.headhunting.domain.member.feign.response.RepoAnalysisResponse;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubService {
    private final GithubRequestClient githubRequestClient;

    public RepoAnalysisResponse repoAnalysis(String owner, String repo, String branch, String token) {
        String requestId = KeyUtils.generateKey();
        return githubRequestClient.repoAnalysis(
                RepoInfoRequest.of(requestId, owner, repo, branch, token)
        );
    }

    public RepoAnalysisResponse getRepoAnalysisStatus(String requestId) {
        return githubRequestClient.repoStatus(requestId);
    }
}
