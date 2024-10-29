package me.noteme.headhunting.domain.resume.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import me.noteme.headhunting.domain.resume.entity.ExperienceType;

import java.time.LocalDate;

@Data
public class ExperienceForm {
    @NotEmpty(message = "이력서 번호는 필수값입니다.")
    private Long resumeId;
    @NotNull(message = "회사, 프로젝트, 대내외활동 등이 작성돼야합니다.")
    private ExperienceType experienceType;
    private String title;
    private String affiliation;
    private LocalDate startAt;
    private LocalDate endAt;
    private String description;
    private String referenceUrl;
    private Long experienceId;
}
