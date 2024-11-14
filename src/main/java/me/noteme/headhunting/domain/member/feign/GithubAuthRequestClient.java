package me.noteme.headhunting.domain.member.feign;

import me.noteme.headhunting.domain.member.feign.request.GitAccessRequest;
import me.noteme.headhunting.domain.member.feign.response.GitAccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "githubAuthRequestClient", url = "https://github.com", configuration = FeignConfig.class)
public interface GithubAuthRequestClient {
    @PostMapping(value = "/login/oauth/access_token")
    GitAccessResponse getAccessToken(@RequestHeader("Accept") String contentType, @RequestBody GitAccessRequest request);
}