package me.noteme.headhunting.domain.recruitment.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.recruitment.dto.CategoryResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentSummaryResponse;
import me.noteme.headhunting.domain.recruitment.service.RecruitmentService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruitments")
public class RecruitmentController {
    private final RecruitmentService recruitmentService;

    @GetMapping
    public SuccessResponse<PagedModel<RecruitmentSummaryResponse>> getRecruitments(
            @LoginUser Long memberId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return SuccessResponse.of(
                new PagedModel<>(recruitmentService.getRecruitments(memberId, pageable))
        );
    }

    @GetMapping("/{recruitmentId}")
    public SuccessResponse<RecruitmentResponse> getRecruitmentById(
            @LoginUser Long memberId,
            @PathVariable("recruitmentId") Long recruitmentId
    ) {
        return SuccessResponse.of(recruitmentService.getRecruitmentById(memberId, recruitmentId));
    }

    @GetMapping("/category/{categoryId}")
    public SuccessResponse<PagedModel<RecruitmentSummaryResponse>> getRecruitmentByCategoryId(
            @LoginUser Long memberId,
            @PathVariable("categoryId") Long categoryId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return SuccessResponse.of(
                new PagedModel<>(recruitmentService.getRecruitmentsByCategoryId(memberId, categoryId, pageable))
        );
    }

    @GetMapping("/category")
    public SuccessResponse<List<CategoryResponse>> getJobCategories(){
        return SuccessResponse.of(recruitmentService.getJobCategories());
    }

    @GetMapping("/{recruitmentId}/scrap")
    public SuccessResponse<Boolean> isScrapped(
            @LoginUser Long memberId,
            @PathVariable("recruitmentId") Long recruitmentId
    ) {
        return SuccessResponse.of(recruitmentService.isScrapped(memberId, recruitmentId));
    }

    @PostMapping("/{recruitmentId}/scrap")
    public SuccessResponse<Void> scrapRecruitment(
            @LoginUser(required = true) Long memberId,
            @PathVariable("recruitmentId") Long recruitmentId
    ) {
        recruitmentService.scrapRecruitment(memberId, recruitmentId);
        return SuccessResponse.empty();
    }

    @DeleteMapping("/{recruitmentId}/scrap")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelScrapRecruitment(
            @LoginUser(required = true) Long memberId,
            @PathVariable("recruitmentId") Long recruitmentId
    ) {
        recruitmentService.unScrapRecruitment(memberId, recruitmentId);
    }

    @GetMapping("/scraps")
    public SuccessResponse<List<Long>> getScrappedRecruitments(
            @LoginUser Long memberId,
            @RequestParam("recruitmentIds") List<Long> recruitmentIds
    ) {
        return SuccessResponse.of(recruitmentService.isScrapped(memberId, recruitmentIds));
    }
}
