package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;
import me.noteme.headhunting.domain.resume.dto.OpenAiResponse;

import java.time.LocalDate;

@Entity
@Table(name = "experience")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experience_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_type")
    @Builder.Default
    private ExperienceType experienceType = ExperienceType.PROJECT;

    private String title;

    private String affiliation;

    @Column(name = "start_at")
    private LocalDate startAt;

    @Column(name = "end_at")
    private LocalDate endAt;

    private String description;

    private String referenceUrl;

    public static Experience of(Long experienceId, ExperienceType experienceType, String title, String affiliation, LocalDate startAt, LocalDate endAt, String description, String referenceUrl, Resume resume) {
        return Experience.builder()
                .id(experienceId)
                .experienceType(experienceType)
                .title(title)
                .affiliation(affiliation)
                .startAt(startAt)
                .endAt(endAt)
                .description(description)
                .referenceUrl(referenceUrl)
                .resume(resume)
                .build();
    }

    public static Experience from(OpenAiResponse.AiExperience experience, Resume resume) {
        return Experience.builder()
                .resume(resume)
                .experienceType(experience.getExperienceType())
                .title(experience.getTitle())
                .affiliation(experience.getAffiliation())
                .startAt(experience.getStartAt())
                .endAt(experience.getEndAt())
                .description(experience.getDescription())
                .referenceUrl(experience.getReferenceUrl())
                .build();
    }
}
