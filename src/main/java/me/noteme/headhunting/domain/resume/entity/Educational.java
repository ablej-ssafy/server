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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String major;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EducationalType category = EducationalType.BACHELOR;

    private String grade;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade_type", nullable = false)
    @Builder.Default
    private GradeType gradeType = GradeType.FOUR_POINT_FIVE;

    private String description;

    @Column(name = "start_at", nullable = false)
    private LocalDate startAt;

    @Column(name = "end_at")
    private LocalDate endAt;
}

