package me.noteme.headhunting.domain.resume.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.domain.resume.controller.request.ReferenceUrlRequest;
import me.noteme.headhunting.domain.resume.dto.TechResponse;
import me.noteme.headhunting.domain.resume.dto.TechSkillResponse;
import me.noteme.headhunting.domain.resume.entity.*;
import me.noteme.headhunting.domain.resume.repository.ResumeRepository;
import me.noteme.headhunting.domain.resume.repository.TechSkillRepository;
import me.noteme.headhunting.domain.resume.repository.TechStackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TechService {
    private final TechSkillRepository techSkillRepository;
    private final TechStackRepository techStackRepository;
    private final ResumeRepository resumeRepository;

    @Transactional
    public void saveTechStack(Long memberId, String githubUrl, String notionUrl, List<Long> techSkills, Long techStackId) {
        Resume resume = getResumeByMemberId(memberId);

        TechStack techStack = TechStack.builder()
                .id(techStackId)
                .githubUrl(githubUrl)
                .notionUrl(notionUrl)
                .resume(resume)
                .build();

        List<TechSkill> techSkillList = techSkillRepository.findAllById(techSkills);

        // TODO: StackSkill 저장 로직 수정 -> 불필요한 쿼리 조회 및 PK 증가 ISSUE
        List<StackSkill> stackSkills = techSkillList.stream()
                .map(techSkill -> StackSkill.builder()
                        .techStack(techStack)
                        .techSkill(techSkill)
                        .build())
                .toList();
        techStack.getStackSkills().addAll(stackSkills);

        techStackRepository.save(techStack);
    }

    @Transactional
    public void saveTechSkill(String name, String iconUrl) {
        TechSkill techSkill = TechSkill.builder()
                .name(name)
                .iconUrl(iconUrl)
                .build();

        techSkillRepository.save(techSkill);
    }

    public TechResponse getTechStack(Long memberId) {
        return techStackRepository.findByMemberId(memberId)
                .map(TechResponse::fromEntity)
                .orElse(null);
    }

    public List<TechSkillResponse> getAllTechSkills() {
        return techSkillRepository.findAll().stream()
                .map(skill -> TechSkillResponse.of(
                        skill.getId(),
                        skill.getName(),
                        skill.getIconUrl()
                ))
                .toList();
    }

    private Resume getResumeByMemberId(Long memberId) {
        return resumeRepository.findByMemberId(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "해당 Member가 지니고 있는 Resume가 없습니다."));
    }
}
