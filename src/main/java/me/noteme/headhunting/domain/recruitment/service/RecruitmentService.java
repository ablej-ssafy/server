package me.noteme.headhunting.domain.recruitment.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.member.entity.Scrap;
import me.noteme.headhunting.domain.member.repository.ScrapRepository;
import me.noteme.headhunting.domain.recruitment.dto.CategoryResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentSummaryResponse;
import me.noteme.headhunting.domain.recruitment.entity.JobCategory;
import me.noteme.headhunting.domain.recruitment.entity.Recruitment;
import me.noteme.headhunting.domain.recruitment.repository.JobCategoryRepository;
import me.noteme.headhunting.domain.recruitment.repository.RecruitmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitmentService {
    private final RecruitmentRepository recruitmentRepository;
    private final ScrapRepository scrapRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final EntityManager em;

    public RecruitmentResponse getRecruitmentById(Long memberId, Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findRecruitmentById(recruitmentId).orElseThrow(
                () -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        // * 프론트엔드에서 ISR 로 조회용 쿼리 날리는 것으로 대체
//        boolean scrapped = scrapRepository.isScrapped(memberId, recruitmentId);

        return RecruitmentResponse.fromEntity(recruitment, false);
    }

    public Page<RecruitmentSummaryResponse> getRecruitmentsByCategoryId(Long memberId, Long categoryId, Pageable pageable) {
        Page<Recruitment> recruitments = recruitmentRepository.findRecruitmentsByCategoryId(categoryId, pageable);
        Set<Long> scrapped = scrapRepository.isScrapped(memberId, recruitments.stream().map(Recruitment::getId).toList());

        return recruitments.map(
                recruitment -> RecruitmentSummaryResponse.fromEntity(recruitment, scrapped.contains(recruitment.getId()))
        );
    }

    public List<CategoryResponse> getJobCategories() {
        return jobCategoryRepository.findAll().stream()
                .filter(jobCategory -> !JobCategory.mainCategoryIds.contains(jobCategory.getId()))
                .map(CategoryResponse::fromEntity)
                .toList();
    }

    @Transactional
    public void scrapRecruitment(Long memberId, Long recruitmentId) {
        if (scrapRepository.isScrapped(memberId, recruitmentId)) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        scrapRepository.save(Scrap.builder()
                .member(em.getReference(Member.class, memberId))
                .recruitment(em.getReference(Recruitment.class, recruitmentId))
                .build());
    }

    @Transactional
    public void unScrapRecruitment(Long memberId, Long recruitmentId) {
        Scrap scrap = scrapRepository.findScrap(memberId, recruitmentId).orElseThrow(
                () -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        scrapRepository.delete(scrap);
    }

    public Page<RecruitmentSummaryResponse> getRecruitments(Long memberId, Pageable pageable) {
        Page<Recruitment> recruitments = recruitmentRepository.findRecruitments(pageable);
        Set<Long> scrapped = scrapRepository.isScrapped(memberId, recruitments.stream().map(Recruitment::getId).toList());

        return recruitments.map(
                recruitment -> RecruitmentSummaryResponse.fromEntity(recruitment, scrapped.contains(recruitment.getId()))
        );
    }

    public boolean isScrapped(Long memberId, Long recruitmentId) {
        return scrapRepository.isScrapped(memberId, recruitmentId);
    }

    public List<Long> isScrapped(Long memberId, List<Long> recruitmentIds) {
        return scrapRepository.isScrapped(memberId, recruitmentIds).stream().toList();
    }
}
