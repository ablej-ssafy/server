package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "education")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "education_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column
    private String name;

    @Column
    private String major;

    @Enumerated(EnumType.STRING)
    @Column
    @Builder.Default
    private EducationType category = EducationType.BACHELOR;

    private String grade;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade_type")
    @Builder.Default
    private GradeType gradeType = GradeType.FOUR_POINT_FIVE;

    private String description;

    @Column(name = "start_at")
    private LocalDate startAt;

    @Column(name = "end_at")
    private LocalDate endAt;

    public static Education of(Long educationId, String name, String major, EducationType category, String grade,
                               GradeType gradeType, String description, LocalDate startAt, LocalDate endAt, Resume resume) {
        Education education = new Education();
        education.id = educationId;
        education.name = name;
        education.major = major;
        education.category = category;
        education.grade = grade;
        education.gradeType = gradeType;
        education.description = description;
        education.startAt = startAt;
        education.endAt = endAt;
        education.resume = resume;
        return education;
    }

    public static Education of(Resume resume) {
        Education education = new Education();
        education.resume = resume;
        education.gradeType = null;
        education.category = null;
        return education;
    }
}

