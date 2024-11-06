package me.noteme.headhunting.domain.recruitment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.listener.event.FileUploadEvent;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.member.repository.MemberRepository;
import me.noteme.headhunting.domain.recruitment.entity.JobCategory;
import me.noteme.headhunting.domain.recruitment.feign.AIRequestClient;
import me.noteme.headhunting.domain.recruitment.feign.request.CompanyInfoRequest;
import me.noteme.headhunting.domain.recruitment.feign.request.JobRecommendRequest;
import me.noteme.headhunting.domain.recruitment.feign.request.PersonalKeywordsRequest;
import me.noteme.headhunting.domain.recruitment.feign.response.AbleJResponse;
import me.noteme.headhunting.domain.recruitment.feign.response.CompanyInfoResponse;
import me.noteme.headhunting.domain.recruitment.feign.response.PersonalKeywordsResponse;
import me.noteme.headhunting.domain.recruitment.feign.response.RecommendResponse;
import me.noteme.headhunting.domain.resume.utils.PDFToTextConverter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final AIRequestClient aiRequestClient;
    private final PDFToTextConverter pdfToTextConverter;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher publisher;

    public List<RecommendResponse> analyzeResume(Long memberId, MultipartFile resumePdf) {
        String resumeText = pdfToTextConverter.convertPdfToText(resumePdf);

        Member member = memberRepository.findFetchById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        JobCategory job = member.getInterestJobs().getFirst().getJobCategory();

        JobRecommendRequest request = JobRecommendRequest.of(
                resumeText,
                member.getCareer(),
                job.getId(),
                job.getName(),
                5
        );

        AbleJResponse<List<RecommendResponse>> resumeRecommend = aiRequestClient.getResumeRecommend(request);
        if (!resumeRecommend.isSuccess()) {
            throw new CustomException(ErrorCode.AI_SERVER_ERROR, resumeRecommend.getError());
        }

        publisher.publishEvent(FileUploadEvent.of(memberId, resumePdf, resumeText));
        return resumeRecommend.getData();
    }

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
            throw new CustomException(ErrorCode.AI_SERVER_ERROR);
        }
        return companyInfo.getCompanyReport();
    }
}
