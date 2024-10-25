package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Educational {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "educational_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    private String name;

    private String major;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String grade;

    @Column(name = "grade_type")
    private String gradeType;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    private String Description;

}

enum Category {
    ASSOCIATE_DEGREE, BACHELOR, MASTER, DOCTOR
}

