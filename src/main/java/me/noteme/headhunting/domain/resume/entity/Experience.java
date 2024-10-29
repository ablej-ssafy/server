package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;

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

    public void updateExperience(ExperienceType experienceType, String title, String affiliation, LocalDate startAt, LocalDate endAt, String description, String referenceUrl) {
        this.experienceType = experienceType;
        this.title = title;
        this.affiliation = affiliation;
        this.startAt = startAt;
        this.endAt = endAt;
        this.description = description;
        this.referenceUrl = referenceUrl;
    }
}
