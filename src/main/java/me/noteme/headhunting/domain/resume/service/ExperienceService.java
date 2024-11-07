package me.noteme.headhunting.domain.resume.service;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.domain.resume.controller.request.ExperienceForm;
import me.noteme.headhunting.domain.resume.dto.EnumTypeResponse;
import me.noteme.headhunting.domain.resume.dto.ExperienceResponse;
import me.noteme.headhunting.domain.resume.entity.Experience;
import me.noteme.headhunting.domain.resume.entity.ExperienceType;
import me.noteme.headhunting.domain.resume.entity.Resume;
import me.noteme.headhunting.domain.resume.repository.ExperienceRepository;
import me.noteme.headhunting.domain.resume.repository.ResumeRepository;
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
    private final ResumeRepository resumeRepository;

    @Transactional
    public void saveAllExperience(Long memberId, List<ExperienceForm> experienceForms) {
        List<Experience> experiences = experienceForms.stream()
                .map(form -> form.toEntity(getResumeByMemberId(memberId)))
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

    @Transactional
    public void deleteById(Long experienceId) {
        experienceRepository.deleteById(experienceId);
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

    private Resume getResumeByMemberId(Long memberId) {
        return resumeRepository.findByMemberId(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "해당 Member가 지니고 있는 Resume가 없습니다."));
    }
}
