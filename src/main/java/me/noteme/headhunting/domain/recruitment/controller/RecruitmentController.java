package me.noteme.headhunting.domain.recruitment.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.domain.recruitment.dto.JobCategoryResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentSummaryResponse;
import me.noteme.headhunting.domain.recruitment.service.RecruitmentService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruitment")
public class RecruitmentController {
    private final RecruitmentService recruitmentService;

    @GetMapping
    public SuccessResponse<PagedModel<RecruitmentSummaryResponse>> searchRecruitments(
            @RequestParam(value = "q", required = false) String query,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return SuccessResponse.of(
                new PagedModel<>(recruitmentService.searchRecruitments(query, pageable))
        );
    }

    @GetMapping("/{recruitmentId}")
    public SuccessResponse<RecruitmentResponse> getRecruitmentById(@PathVariable("recruitmentId") Long recruitmentId) {
        return SuccessResponse.of(recruitmentService.getRecruitmentById(recruitmentId));
    }

    @GetMapping("/category/{categoryId}")
    public SuccessResponse<PagedModel<RecruitmentSummaryResponse>> getRecruitmentByCategoryId(
            @PathVariable("categoryId") Long categoryId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return SuccessResponse.of(
                new PagedModel<>(recruitmentService.getRecruitmentsByCategoryId(categoryId, pageable))
        );
    }

    @GetMapping("/category")
    public SuccessResponse<List<JobCategoryResponse>> getJobCategories(){
        return SuccessResponse.of(recruitmentService.getJobCategories());
    }
}
