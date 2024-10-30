package me.noteme.headhunting.domain.resume.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.noteme.headhunting.domain.resume.entity.EducationalType;
import me.noteme.headhunting.domain.resume.entity.GradeType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class EducationalForm {
    @Min(value = 1, message = "이력서 번호는 필수값입니다.(최소 1)")
    private Long resumeId;
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
    private EducationalType category;
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
    private Long educationalId;
}
