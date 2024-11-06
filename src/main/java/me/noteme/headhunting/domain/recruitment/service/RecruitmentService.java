package me.noteme.headhunting.domain.recruitment.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.member.entity.Scrap;
import me.noteme.headhunting.domain.member.repository.ScrapRepository;
import me.noteme.headhunting.domain.recruitment.dto.JobCategoryResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentSummaryResponse;
import me.noteme.headhunting.domain.recruitment.entity.Recruitment;
import me.noteme.headhunting.domain.recruitment.repository.JobCategoryRepository;
import me.noteme.headhunting.domain.recruitment.repository.RecruitmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitmentService {
    private final RecruitmentRepository recruitmentRepository;
    private final ScrapRepository scrapRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final EntityManager em;

    public RecruitmentResponse getRecruitmentById(Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findRecruitmentById(recruitmentId).orElseThrow(
                () -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        return RecruitmentResponse.fromEntity(recruitment);
    }

    public Page<RecruitmentSummaryResponse> getRecruitmentsByCategoryId(Long categoryId, Pageable pageable) {
        Page<Recruitment> recruitments = recruitmentRepository.findRecruitmentsByCategoryId(categoryId, pageable);
        return recruitments.map(RecruitmentSummaryResponse::fromEntity);
    }

    public List<JobCategoryResponse> getJobCategories() {
        return jobCategoryRepository.findAll().stream()
                .map(JobCategoryResponse::fromEntity)
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
}
