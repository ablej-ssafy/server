package me.noteme.headhunting.domain.recruitment.feign;

import me.noteme.headhunting.domain.recruitment.feign.request.CompanyInfoRequest;
import me.noteme.headhunting.domain.recruitment.feign.request.PersonalKeywordsRequest;
import me.noteme.headhunting.domain.recruitment.feign.response.CompanyInfoResponse;
import me.noteme.headhunting.domain.recruitment.feign.response.PersonalKeywordsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AIRequestClient", url = "${app.ai-base-url}")
public interface AIRequestClient {

    @PostMapping("/personal_keywords")
    PersonalKeywordsResponse getPersonalKeywords(@RequestBody PersonalKeywordsRequest request);

    @PostMapping("/company_reports")
    CompanyInfoResponse getCompanyInfo(@RequestBody CompanyInfoRequest request);
}
