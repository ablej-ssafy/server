package me.noteme.headhunting.domain.resume.service;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.domain.resume.controller.request.EducationalForm;
import me.noteme.headhunting.domain.resume.dto.EducationalResponse;
import me.noteme.headhunting.domain.resume.dto.EnumTypeResponse;
import me.noteme.headhunting.domain.resume.entity.Educational;
import me.noteme.headhunting.domain.resume.entity.EducationalType;
import me.noteme.headhunting.domain.resume.entity.Resume;
import me.noteme.headhunting.domain.resume.repository.EducationalRepository;
import me.noteme.headhunting.domain.resume.repository.ResumeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EducationalService {
    private final EducationalRepository educationalRepository;
    private final ResumeRepository resumeRepository;

    @Transactional
    public void saveAllEducationals(Long memberId, List<EducationalForm> educationalForms) {
        List<Educational> educationals = educationalForms.stream()
                .map(form -> form.toEntity(getResumeByMemberId(memberId)))
                .toList();

        educationalRepository.saveAll(educationals);
    }

    public EducationalResponse getAllEducationals(Long memberId) {
        return EducationalResponse.of(educationalRepository.findAllByMemberId(memberId).stream()
                .map(EducationalForm::fromEntity)
                .toList()
        );
    }

    public List<EnumTypeResponse> getEducationTypes() {
        return Arrays.stream(EducationalType.values())
                .map(type -> EnumTypeResponse.of(type.name(), type.getName()))
                .toList();
    }

    @Transactional
    public void deleteById(Long educationId) {
        educationalRepository.deleteById(educationId);
    }

    @Transactional
    public EducationalForm createEmptyEducational(Long memberId) {
        return EducationalForm.fromEntity(educationalRepository.save(
                Educational.of(getResumeByMemberId(memberId))
        ));
    }

    private Resume getResumeByMemberId(Long memberId) {
        return resumeRepository.findByMemberId(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "해당 Member가 지니고 있는 Resume가 없습니다."));
    }
}
