package me.noteme.headhunting.domain.resume.service;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.domain.resume.controller.request.EducationForm;
import me.noteme.headhunting.domain.resume.dto.EducationResponse;
import me.noteme.headhunting.domain.resume.dto.EnumTypeResponse;
import me.noteme.headhunting.domain.resume.entity.Education;
import me.noteme.headhunting.domain.resume.entity.EducationType;
import me.noteme.headhunting.domain.resume.entity.Resume;
import me.noteme.headhunting.domain.resume.entity.mongo.MongoEducation;
import me.noteme.headhunting.domain.resume.repository.EducationRepository;
import me.noteme.headhunting.domain.resume.repository.MongoResumeRepository;
import me.noteme.headhunting.domain.resume.repository.ResumeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EducationService {
    private final EducationRepository educationRepository;
    private final MongoResumeRepository mongoResumeRepository;
    private final ResumeRepository resumeRepository;

    @Transactional
    public void saveAllEducations(Long memberId, List<EducationForm> educationForms) {
        List<Education> educations = educationForms.stream()
                .map(form -> form.toEntity(getResumeByMemberId(memberId)))
                .toList();

        mongoResumeRepository.updateEducations(
                memberId,
                educationRepository.saveAll(educations).stream().map(MongoEducation::from).toList()
        );
    }

    public EducationResponse getAllEducations(Long memberId) {
        return EducationResponse.of(educationRepository.findAllByMemberId(memberId).stream()
                .map(EducationForm::fromEntity)
                .toList()
        );
    }

    public List<EnumTypeResponse> getEducationTypes() {
        return Arrays.stream(EducationType.values())
                .map(type -> EnumTypeResponse.of(type.name(), type.getName()))
                .toList();
    }

    @Transactional
    public void deleteById(Long educationId) {
        educationRepository.deleteById(educationId);
    }

    @Transactional
    public EducationForm createEmptyEducation(Long memberId) {
        return EducationForm.fromEntity(educationRepository.save(
                Education.of(getResumeByMemberId(memberId))
        ));
    }

    private Resume getResumeByMemberId(Long memberId) {
        return resumeRepository.findByMemberId(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "해당 Member가 지니고 있는 Resume가 없습니다."));
    }
}
