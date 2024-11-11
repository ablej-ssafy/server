package me.noteme.headhunting.domain.resume.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.noteme.headhunting.domain.resume.entity.Education;
import me.noteme.headhunting.domain.resume.entity.EducationType;
import me.noteme.headhunting.domain.resume.entity.GradeType;
import me.noteme.headhunting.domain.resume.entity.Resume;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class EducationForm {
    /**
     * 학교 이름
     */
    private String name;

    /**
     * 전공
     */
    private String major;

    /**
     * 학교 분류
     * ASSOCIATE_DEGREE, BACHELOR, MASTER, DOCTOR
     */
    private EducationType category;

    /**
     * 학점 평균
     */
    private String grade;

    /**
     * 최대 학점
     * FOUR_POINT_ZERO, FOUR_POINT_THREE, FOUR_POINT_FIVE
     */
    private GradeType gradeType;

    /**
     * 학부 활동 설명
     */
    private String description;

    private LocalDate startAt;

    private LocalDate endAt;

    private Long educationId;

    public Education toEntity(Resume resume) {
        return Education.of(
                this.educationId,
                this.name,
                this.major,
                this.category,
                this.grade,
                this.gradeType,
                this.description,
                this.startAt,
                this.endAt,
                resume
        );
    }

    public static EducationForm fromEntity(Education education) {
        return EducationForm.of(
                education.getName(),
                education.getMajor(),
                education.getCategory(),
                education.getGrade(),
                education.getGradeType(),
                education.getDescription(),
                education.getStartAt(),
                education.getEndAt(),
                education.getId()
        );
    }

}
