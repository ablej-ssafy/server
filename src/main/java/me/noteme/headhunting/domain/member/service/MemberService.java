package me.noteme.headhunting.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.domain.member.dto.LoginMemberResponse;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.member.repository.MemberRepository;
import me.noteme.headhunting.domain.member.repository.ScrapRepository;
import me.noteme.headhunting.domain.recruitment.dto.CategoryResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentSummaryResponse;
import me.noteme.headhunting.domain.recruitment.entity.JobCategory;
import me.noteme.headhunting.domain.recruitment.repository.JobCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ScrapRepository scrapRepository;
    private final JobCategoryRepository jobCategoryRepository;

    public LoginMemberResponse info(long memberId) {
        Member member = getMember(memberId);

        if (!member.isEmailVerified()) {
            throw new CustomException(ErrorCode.ACCESS_DENIED, "이메일 인증을 완료해주세요");
        }

        return LoginMemberResponse.of(
                member.getId(),
                member.getNickname(),
                member.getUsername(),
                member.getCareer(),
                CategoryResponse.fromEntity(member.getJobCategory())
        );
    }

    public List<RecruitmentSummaryResponse> scrapList(Long memberId) {
        return scrapRepository.findAllByMemberId(memberId).stream()
                .map(recruitment -> RecruitmentSummaryResponse.fromEntity(recruitment, true))
                .toList();
    }

    @Transactional
    public void updateJobCategory(Long memberId, Long jobCategoryId) {
        Member member = getMember(memberId);
        JobCategory jobCategory = jobCategoryRepository.findById(jobCategoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 직무입니다."));

        member.chagneJobCategory(jobCategory);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findFetchById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
    }
}
