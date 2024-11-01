package me.noteme.headhunting.domain.resume.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.domain.resume.controller.request.ReferenceUrlRequest;
import me.noteme.headhunting.domain.resume.dto.TechResponse;
import me.noteme.headhunting.domain.resume.dto.TechSkillResponse;
import me.noteme.headhunting.domain.resume.entity.*;
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
    private final EntityManager em;

    @Transactional
    public void saveTechStack(Long resumeId, List<ReferenceUrlRequest> urls, List<Long> techSkills, Long techStackId) {
        Resume resume = em.getReference(Resume.class, resumeId);

        TechStack techStack = TechStack.builder()
                .id(techStackId)
                .resume(resume)
                .build();

        // TODO: ReferenceUrl 저장 로직 수정 -> 불필요한 쿼리 조회 및 PK 증가 ISSUE
        List<ReferenceUrl> referenceUrls = urls.stream()
                .map(dto -> ReferenceUrl.builder()
                        .techStack(techStack)
                        .id(dto.getId())
                        .url(dto.getUrl())
                        .build())
                .toList();

        techStack.getReferenceUrls().addAll(referenceUrls);

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
        TechStack techStack = techStackRepository.findByMemberId(memberId);

        return TechResponse.fromEntity(techStack);
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
}
