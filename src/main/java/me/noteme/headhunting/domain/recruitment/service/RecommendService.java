package me.noteme.headhunting.domain.recruitment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.listener.event.FileUploadEvent;
import me.noteme.headhunting.common.service.StorageService;
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
import me.noteme.headhunting.domain.resume.entity.ResumePdf;
import me.noteme.headhunting.domain.resume.repository.ResumePdfRepository;
import me.noteme.headhunting.domain.resume.utils.PDFToTextConverter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final AIRequestClient aiRequestClient;
    private final MemberRepository memberRepository;
    private final ResumePdfRepository resumePdfRepository;
    private final StorageService storageService;

    public List<RecommendResponse> analyzeResume(Long memberId, Long resumePdfId) {
        ResumePdf resumePdf = resumePdfRepository.findByIdAndMemberId(resumePdfId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        String resumeText = storageService.getData(memberId + "/" + resumePdf.getKey());

        Member member = memberRepository.findFetchById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        JobCategory job = member.getInterestJobs().getFirst().getJobCategory();

        JobRecommendRequest request = JobRecommendRequest.of(
                resumeText,
                member.getCareer(),
                job.getId(),
                job.getName(),
                10
        );

        AbleJResponse<List<RecommendResponse>> resumeRecommend = aiRequestClient.getResumeRecommend(request);
        if (!resumeRecommend.isSuccess()) {
            throw new CustomException(ErrorCode.AI_SERVER_ERROR, resumeRecommend.getError());
        }

        return resumeRecommend.getData();
    }

    public List<String> getResumeKeywords(int jobId, int jobSubId, String resume) {
        PersonalKeywordsRequest request = PersonalKeywordsRequest.of(jobId, jobSubId, resume);

        PersonalKeywordsResponse personalKeywords = aiRequestClient.getPersonalKeywords(request);
        return personalKeywords.getMessageAsList();
    }

    public String getCompanyAnalyze(String companyName) {
        CompanyInfoRequest request = CompanyInfoRequest.of(companyName);
        CompanyInfoResponse companyInfo = aiRequestClient.getCompanyInfo(request);
        if (Objects.isNull(companyInfo) || !companyInfo.isSuccess()) {
            throw new CustomException(ErrorCode.AI_SERVER_ERROR);
        }
        return companyInfo.getCompanyReport();
    }
}
