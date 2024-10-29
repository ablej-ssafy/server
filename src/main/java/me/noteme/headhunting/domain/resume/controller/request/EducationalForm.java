package me.noteme.headhunting.domain.resume.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import me.noteme.headhunting.domain.resume.entity.EducationalType;
import me.noteme.headhunting.domain.resume.entity.GradeType;

import java.time.LocalDate;

@Data
public class EducationalForm {
    @NotEmpty(message = "이력서 번호는 필수값입니다.")
    private Long resumeId;
    private String name;
    private String major;
    private EducationalType category;
    private String grade;
    private GradeType gradeType;
    private String description;
    private LocalDate startAt;
    private LocalDate endAt;
    private Long educationalId;
}
