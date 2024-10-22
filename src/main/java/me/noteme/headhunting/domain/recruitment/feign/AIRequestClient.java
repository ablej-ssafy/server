package me.noteme.headhunting.domain.recruitment.feign;

import me.noteme.headhunting.domain.recruitment.feign.response.*;
import me.noteme.headhunting.domain.recruitment.feign.request.CompanyInfoRequest;
import me.noteme.headhunting.domain.recruitment.feign.request.JobRecommendRequest;
import me.noteme.headhunting.domain.recruitment.feign.request.PersonalKeywordsRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AIRequestClient", url = "${app.ai-base-url}")
public interface AIRequestClient {

    @PostMapping("/personal_keywords")
    PersonalKeywordsResponse getPersonalKeywords(@RequestBody PersonalKeywordsRequest request);

    @PostMapping("/company_reports")
    CompanyInfoResponse getCompanyInfo(@RequestBody CompanyInfoRequest request);

    /**
     * 합격 가능성 높은 채용공고 추천 API
     * 요청 Type 2가지 분류
     */
    @PostMapping("/job-postings/recommend")
    AbleJResponse<RecommendResponse> getResumeRecommend(@RequestBody JobRecommendRequest request);
//    AbleJResponse<ResumeFitnessResponse> getJobRecommend(@RequestBody JobRecommendRequest request);

}
