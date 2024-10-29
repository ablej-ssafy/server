package me.noteme.headhunting.domain.resume.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.domain.resume.controller.request.EducationalForm;
import me.noteme.headhunting.domain.resume.entity.Educational;
import me.noteme.headhunting.domain.resume.entity.EducationalType;
import me.noteme.headhunting.domain.resume.entity.GradeType;
import me.noteme.headhunting.domain.resume.entity.Resume;
import me.noteme.headhunting.domain.resume.repository.EducationalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationalService {
    private final EducationalRepository educationalRepository;
    private final EntityManager em;

    @Transactional
    public void saveEducational(Long resumeId, String name, String major, EducationalType category, String grade, GradeType gradeType, String description, LocalDate startAt, LocalDate endAt, Long educationalId) {
        Resume resume = getResumeById(resumeId);

        Educational educational = generateEducational(educationalId, name, major, category, grade, gradeType, description, startAt, endAt, resume);

        educationalRepository.save(educational);
    }

    public void saveAllEducationals(List<EducationalForm> educationalForms) {
        List<Educational> educationals = educationalForms.stream()
                .map(form -> generateEducational(
                        form.getEducationalId(),
                        form.getName(),
                        form.getMajor(),
                        form.getCategory(),
                        form.getGrade(),
                        form.getGradeType(),
                        form.getDescription(),
                        form.getStartAt(),
                        form.getEndAt(),
                        getResumeById(form.getResumeId())
                ))
                .toList();
    }

    private Resume getResumeById(Long resumeId) {
        return em.getReference(Resume.class, resumeId);
    }

    private static Educational generateEducational(Long educationalId, String name, String major, EducationalType category, String grade, GradeType gradeType, String description, LocalDate startAt, LocalDate endAt, Resume resume) {
        return Educational.builder()
                .id(educationalId)
                .resume(resume)
                .name(name)
                .major(major)
                .category(category)
                .grade(grade)
                .gradeType(gradeType)
                .description(description)
                .startAt(startAt)
                .endAt(endAt)
                .build();
    }
}
