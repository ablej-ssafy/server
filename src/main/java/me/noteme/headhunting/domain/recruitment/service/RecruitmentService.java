package me.noteme.headhunting.domain.recruitment.service;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.domain.recruitment.feign.AIRequestClient;
import me.noteme.headhunting.domain.recruitment.feign.request.CompanyInfoRequest;
import me.noteme.headhunting.domain.recruitment.feign.request.PersonalKeywordsRequest;
import me.noteme.headhunting.domain.recruitment.feign.response.CompanyInfoResponse;
import me.noteme.headhunting.domain.recruitment.feign.response.PersonalKeywordsResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitmentService {
    private final AIRequestClient aiRequestClient;

    public List<String> getResumeKeywords(int jobId, int jobSubId, String resume) {
        PersonalKeywordsRequest request = new PersonalKeywordsRequest();
        request.setJobId(jobId);
        request.setJobSubId(jobSubId);
        request.setResume(resume);

        PersonalKeywordsResponse personalKeywords = aiRequestClient.getPersonalKeywords(request);
        return personalKeywords.getMessageAsList();
    }

    public String getCompanyAnalyze(String companyName) {
        CompanyInfoRequest request = new CompanyInfoRequest();
        request.setCompanyName(companyName);
        CompanyInfoResponse companyInfo = aiRequestClient.getCompanyInfo(request);
        if (companyInfo == null || !companyInfo.isSuccess()) {
            throw new RuntimeException("Failed to load companyInfo");
        }
        return companyInfo.getCompanyReport();
    }
}
