package me.noteme.headhunting.domain.member.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.common.utils.CookieUtils;
import me.noteme.headhunting.domain.member.controller.request.GitAnalysisResultRequest;
import me.noteme.headhunting.domain.member.feign.GithubAuthRequestClient;
import me.noteme.headhunting.domain.member.feign.request.GitAccessRequest;
import me.noteme.headhunting.domain.member.feign.request.RepoInfoRequest;
import me.noteme.headhunting.domain.member.feign.response.GitAccessResponse;
import me.noteme.headhunting.domain.member.feign.response.RepoAnalysisResponse;
import me.noteme.headhunting.domain.member.service.GithubService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/github")
@RequiredArgsConstructor
public class GithubController {
    private final GithubService githubService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final GithubAuthRequestClient gitHubAuthRequestClient;

    @GetMapping("/authorize")
    public void authorize(HttpServletResponse response, @RequestParam("redirect_url") String redirectUrl) throws IOException {
        String clientId = clientRegistrationRepository.findByRegistrationId("github").getClientId();
        String redirectUri = clientRegistrationRepository.findByRegistrationId("github").getRedirectUri();

        String githubRedirectUrl = UriComponentsBuilder.fromHttpUrl("https://github.com/login/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", "repo")
                .queryParam("state", redirectUrl)
                .toUriString();

        response.sendRedirect(githubRedirectUrl);
    }

    @GetMapping("/callback")
    public void callback(@RequestParam("code") String code, @RequestParam("state") String redirectUrl, HttpServletResponse response) throws IOException {
        String clientId = clientRegistrationRepository.findByRegistrationId("github").getClientId();
        String clientSecret = clientRegistrationRepository.findByRegistrationId("github").getClientSecret();
        String redirectUri = clientRegistrationRepository.findByRegistrationId("github").getRedirectUri();

        GitAccessRequest gitRequest = GitAccessRequest.of(clientId, clientSecret, code, redirectUri);

        GitAccessResponse gitAccessToken = gitHubAuthRequestClient.getAccessToken("application/json", gitRequest);

        response.sendRedirect(redirectUrl + "?accessToken=" + gitAccessToken.getAccessToken());
    }

    @PostMapping("/analysis")
    public SuccessResponse<RepoAnalysisResponse> gitAnalysis(
            @LoginUser Long memberId,
            @Validated @RequestBody RepoInfoRequest request
    ) {
        return SuccessResponse.of(
                githubService.repoAnalysis(memberId, request.getOwner(), request.getRepo(), request.getBranch(), request.getToken())
        );
    }

    @GetMapping("/analysis/{requestId}")
    public SuccessResponse<RepoAnalysisResponse> getAnalysisInfo(@PathVariable("requestId") String requestId) {
        return SuccessResponse.of(
                githubService.getRepoAnalysisStatus(requestId)
        );
    }

    @PostMapping("/analysis/result")
    public SuccessResponse<Void> sendAnalysisResult(@Validated @RequestBody GitAnalysisResultRequest request) {
        githubService.sendAnalysisResult(request.getEmail(), request.getRepositoryName(), request.getAnalysisSummary());
        return SuccessResponse.empty();
    }
}
