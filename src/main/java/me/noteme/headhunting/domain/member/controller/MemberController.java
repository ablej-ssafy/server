package me.noteme.headhunting.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.member.controller.request.JobCategoryRequest;
import me.noteme.headhunting.domain.member.dto.LoginMemberResponse;
import me.noteme.headhunting.domain.member.service.MemberService;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentSummaryResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public SuccessResponse<LoginMemberResponse> currentMemberInfo(@LoginUser Long memberId) {
        return SuccessResponse.of(memberService.info(memberId));
    }

    @GetMapping("/scrap")
    public SuccessResponse<List<RecruitmentSummaryResponse>> scrapList(@LoginUser Long memberId) {
        return SuccessResponse.of(memberService.scrapList(memberId));
    }

    @PatchMapping("/category")
    public SuccessResponse<Void> updateJobCategory(@LoginUser Long memberId, @Validated @RequestBody JobCategoryRequest jobCategoryId) {
        memberService.updateJobCategory(memberId, jobCategoryId.getId());

        return SuccessResponse.empty();
    }
}
