package me.noteme.headhunting.domain.resume.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.domain.resume.controller.request.ExperienceForm;
import me.noteme.headhunting.domain.resume.dto.EnumTypeResponse;
import me.noteme.headhunting.domain.resume.dto.ExperienceResponse;
import me.noteme.headhunting.domain.resume.entity.Experience;
import me.noteme.headhunting.domain.resume.entity.ExperienceType;
import me.noteme.headhunting.domain.resume.entity.Resume;
import me.noteme.headhunting.domain.resume.repository.ExperienceRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperienceService {
    private final ExperienceRepository experienceRepository;
    private final EntityManager em;

    @Transactional
    public void saveExperience(Long resumeId, ExperienceType experienceType, String title, String affiliation, LocalDate startAt, LocalDate endAt, String description, String referenceUrl, Long experienceId) {
        Resume resume = getResumeById(resumeId);

        Experience experience = generateExperience(experienceId, experienceType, title, affiliation, startAt, endAt, description, referenceUrl, resume);

        experienceRepository.save(experience);
    }

    @Transactional
    public void saveAllExperience(List<ExperienceForm> experienceForms) {
        List<Experience> experiences = experienceForms.stream()
                .map(form -> form.toEntity(getResumeById(form.getResumeId())))
                .toList();

        experienceRepository.saveAll(experiences);
    }

    public ExperienceResponse getExperiences(Long memberId, String type) {
        List<Experience> experiences;
        if (StringUtils.isEmpty(type)) {
            return ExperienceResponse.of(getExperienceForms(experienceRepository.findAllByMemberId(memberId)));
        }

        ExperienceType experienceType = ExperienceType.from(type);
        experiences = experienceRepository.findAllByMemberIdAndType(memberId, experienceType);
        return ExperienceResponse.of(getExperienceForms(experiences));
    }

    public List<EnumTypeResponse> getExperienceTypes() {
        return Arrays.stream(ExperienceType.values())
                .map(type -> EnumTypeResponse.of(type.name(), type.getValue()))
                .toList();
    }

    private List<ExperienceForm> getExperienceForms(List<Experience> experiences) {
        return experiences.stream()
                .map(ExperienceForm::fromEntity)
                .toList();
    }

    private Resume getResumeById(Long resumeId) {
        return em.getReference(Resume.class, resumeId);
    }

    private static Experience generateExperience(Long experienceId, ExperienceType experienceType, String title, String affiliation, LocalDate startAt, LocalDate endAt, String description, String referenceUrl, Resume resume) {
        return Experience.builder()
                .id(experienceId)
                .resume(resume)
                .experienceType(experienceType)
                .title(title)
                .affiliation(affiliation)
                .startAt(startAt)
                .endAt(endAt)
                .description(description)
                .referenceUrl(referenceUrl)
                .build();
    }
}
