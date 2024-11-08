package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "educational")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Educational {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "educational_id")
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
    private EducationalType category = EducationalType.BACHELOR;

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

    public static Educational of(Long educationalId, String name, String major, EducationalType category, String grade,
                                 GradeType gradeType, String description, LocalDate startAt, LocalDate endAt, Resume resume) {
        Educational educational = new Educational();
        educational.id = educationalId;
        educational.name = name;
        educational.major = major;
        educational.category = category;
        educational.grade = grade;
        educational.gradeType = gradeType;
        educational.description = description;
        educational.startAt = startAt;
        educational.endAt = endAt;
        educational.resume = resume;
        return educational;
    }

    public static Educational of(Resume resume) {
        Educational educational = new Educational();
        educational.resume = resume;
        educational.gradeType = null;
        educational.category = null;
        return educational;
    }
}

