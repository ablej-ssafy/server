package me.noteme.headhunting.domain.recruitment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentResponse;
import me.noteme.headhunting.domain.recruitment.dto.RecruitmentSummaryResponse;
import me.noteme.headhunting.domain.recruitment.entity.Recruitment;
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

    public Page<RecruitmentSummaryResponse> searchRecruitments(String query, Pageable pageable) {
        Page<Recruitment> recruitments = recruitmentRepository.searchRecruitments(query, pageable);
        return recruitments.map(RecruitmentSummaryResponse::fromEntity);
    }
}
