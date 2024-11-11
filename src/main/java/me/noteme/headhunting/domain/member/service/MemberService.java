package me.noteme.headhunting.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.domain.member.dto.LoginMemberResponse;
import me.noteme.headhunting.domain.member.entity.InterestJob;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.member.repository.MemberRepository;
import me.noteme.headhunting.domain.member.repository.ScrapRepository;
import me.noteme.headhunting.domain.recruitment.dto.CategoryResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentSummaryResponse;
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

    public LoginMemberResponse info(long memberId) {
        Member member = memberRepository.findFetchById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!member.isEmailVerified()) {
            throw new CustomException(ErrorCode.ACCESS_DENIED, "이메일 인증을 완료해주세요");
        }

        List<InterestJob> interestJobs = member.getInterestJobs();
        List<CategoryResponse> categories = interestJobs.stream()
                .map(InterestJob::getJobCategory)
                .map(CategoryResponse::fromEntity)
                .toList();

        return LoginMemberResponse.of(
                member.getId(),
                member.getNickname(),
                member.getUsername(),
                member.getCareer(),
                categories
        );
    }

    public List<RecruitmentSummaryResponse> scrapList(Long memberId) {
        return scrapRepository.findAllByMemberId(memberId).stream()
                .map(RecruitmentSummaryResponse::fromEntity)
                .toList();
    }
}
