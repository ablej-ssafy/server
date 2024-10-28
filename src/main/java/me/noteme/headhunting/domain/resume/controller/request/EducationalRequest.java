package me.noteme.headhunting.domain.resume.controller.request;

import lombok.Data;
import me.noteme.headhunting.domain.resume.entity.EducationalType;
import me.noteme.headhunting.domain.resume.entity.GradeType;

import java.time.LocalDate;

@Data
public class EducationalRequest {
    private String name;
    private String major;
    private EducationalType category;
    private String grade;
    private GradeType gradeType;
    private String description;
    private LocalDate startAt;
    private LocalDate endAt;
}
