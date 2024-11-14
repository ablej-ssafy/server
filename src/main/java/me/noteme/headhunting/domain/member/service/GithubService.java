package me.noteme.headhunting.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.listener.event.GithubAnalysisEvent;
import me.noteme.headhunting.common.utils.KeyUtils;
import me.noteme.headhunting.domain.member.dto.AnalysisSummary;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.member.feign.GithubRequestClient;
import me.noteme.headhunting.domain.member.feign.request.RepoInfoRequest;
import me.noteme.headhunting.domain.member.feign.response.RepoAnalysisResponse;
import me.noteme.headhunting.domain.member.repository.MemberRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubService {
    private final GithubRequestClient githubRequestClient;
    private final ApplicationEventPublisher publisher;
    private final MemberRepository memberRepository;

    public RepoAnalysisResponse repoAnalysis(Long memberId, String owner, String repo, String branch, String token) {
        String requestId = KeyUtils.generateKey();
        return githubRequestClient.repoAnalysis(
                RepoInfoRequest.of(requestId, owner, repo, branch, token, String.valueOf(memberId))
        );
    }

    public RepoAnalysisResponse getRepoAnalysisStatus(String requestId) {
        return githubRequestClient.repoStatus(requestId);
    }

    public void sendAnalysisResult(String memberId, String repositoryName, AnalysisSummary analysisSummary) {
        Member member = memberRepository.findFetchById(Long.parseLong(memberId))
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        publisher.publishEvent(GithubAnalysisEvent.of(member.getUsername(), member.getNickname(), repositoryName, analysisSummary));
    }
}
