package me.noteme.headhunting.domain.resume.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.domain.resume.controller.request.EducationalForm;
import me.noteme.headhunting.domain.resume.dto.EducationalResponse;
import me.noteme.headhunting.domain.resume.dto.EnumTypeResponse;
import me.noteme.headhunting.domain.resume.entity.Educational;
import me.noteme.headhunting.domain.resume.entity.EducationalType;
import me.noteme.headhunting.domain.resume.entity.GradeType;
import me.noteme.headhunting.domain.resume.entity.Resume;
import me.noteme.headhunting.domain.resume.repository.EducationalRepository;
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
    private final EntityManager em;

    @Transactional
    public void saveAllEducationals(List<EducationalForm> educationalForms) {
        List<Educational> educationals = educationalForms.stream()
                .map(form -> form.toEntity(getResumeById(form.getResumeId())))
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

    private Resume getResumeById(Long resumeId) {
        return em.getReference(Resume.class, resumeId);
    }
}
