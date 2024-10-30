package me.noteme.headhunting.domain.resume.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.domain.resume.controller.request.ReferenceUrlResponse;
import me.noteme.headhunting.domain.resume.entity.*;
import me.noteme.headhunting.domain.resume.repository.TechSkillRepository;
import me.noteme.headhunting.domain.resume.repository.TechStackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechService {
    private final TechSkillRepository techSkillRepository;
    private final TechStackRepository techStackRepository;
    private final EntityManager em;

    @Transactional
    public void saveTechStack(Long resumeId, List<ReferenceUrlResponse> urls, List<Long> techSkills, Long techStackId) {
        Resume resume = em.getReference(Resume.class, resumeId);

        TechStack techStack = TechStack.builder()
                .id(techStackId)
                .resume(resume)
                .build();

        List<ReferenceUrl> referenceUrls = urls.stream()
                .map(dto -> ReferenceUrl.builder()
                        .techStack(techStack)
                        .id(dto.getId())
                        .url(dto.getUrl())
                        .build())
                .toList();

        techStack.getReferenceUrls().addAll(referenceUrls);

        List<TechSkill> techSkillList = techSkillRepository.findAllById(techSkills);
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
}
