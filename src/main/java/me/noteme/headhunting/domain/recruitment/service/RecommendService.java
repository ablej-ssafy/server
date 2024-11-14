package me.noteme.headhunting.domain.recruitment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.service.StorageService;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.member.repository.MemberRepository;
import me.noteme.headhunting.domain.member.repository.ScrapRepository;
import me.noteme.headhunting.domain.recruitment.dto.RecommendResponse;
import me.noteme.headhunting.domain.recruitment.entity.JobCategory;
import me.noteme.headhunting.domain.recruitment.feign.AIRequestClient;
import me.noteme.headhunting.domain.recruitment.feign.request.CompanyInfoRequest;
import me.noteme.headhunting.domain.recruitment.feign.request.JobRecommendRequest;
import me.noteme.headhunting.domain.recruitment.feign.request.PersonalKeywordsRequest;
import me.noteme.headhunting.domain.recruitment.feign.response.AbleJResponse;
import me.noteme.headhunting.domain.recruitment.feign.response.CompanyInfoResponse;
import me.noteme.headhunting.domain.recruitment.feign.response.PersonalKeywordsResponse;
import me.noteme.headhunting.domain.recruitment.feign.response.AiRecommendResponse;
import me.noteme.headhunting.domain.recruitment.repository.RecruitmentRepository;
import me.noteme.headhunting.domain.resume.entity.ResumePdf;
import me.noteme.headhunting.domain.resume.repository.ResumePdfRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final ResumePdfRepository resumePdfRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final ScrapRepository scrapRepository;
    private final AIRequestClient aiRequestClient;
    private final MemberRepository memberRepository;
    private final StorageService storageService;

    private final int SIZE = 24;

    public List<RecommendResponse> analyzeResume(Long memberId, Long resumePdfId) {
        List<AiRecommendResponse> recommendResponses = getAiRecommend(memberId, resumePdfId);

        Map<Long, Double> recruitmentMap = new HashMap<>();
        List<Long> recommendIds = recommendResponses.stream()
                .map(response -> {
                    recruitmentMap.put(response.getId(), response.getSimilarity());
                    return response.getId();
                })
                .toList();

        Set<Long> scrappedList = scrapRepository.isScrapped(memberId, recommendIds);

        return recruitmentRepository.findRecruitmentsById(recommendIds).stream()
                .map(recruitment -> RecommendResponse.create(
                        recruitment,
                        scrappedList.contains(recruitment.getId()),
                        recruitmentMap.get(recruitment.getId())
                ))
                .sorted()
                .toList();
    }

    private List<AiRecommendResponse> getAiRecommend(Long memberId, Long resumePdfId) {
        ResumePdf resumePdf = getResumePdf(memberId, resumePdfId);
        Member member = getMember(memberId);

        String resumeText = storageService.getData(memberId + "/" + resumePdf.getKey());
        JobCategory job = member.getJobCategory();

        JobRecommendRequest request = JobRecommendRequest.of(
                resumeText, member.getCareer(), job.getId(), SIZE
        );

        AbleJResponse<List<AiRecommendResponse>> resumeRecommend = aiRequestClient.getResumeRecommend(request);
        if (!resumeRecommend.isSuccess()) {
            throw new CustomException(ErrorCode.AI_SERVER_ERROR, resumeRecommend.getError());
        }

        return resumeRecommend.getData();
    }

    private Member getMember(Long memberId) {
        return memberRepository.findFetchById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    private ResumePdf getResumePdf(Long memberId, Long resumePdfId) {
        return resumePdfRepository.findByIdAndMemberId(resumePdfId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
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
