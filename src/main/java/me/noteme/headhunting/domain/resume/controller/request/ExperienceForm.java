package me.noteme.headhunting.domain.resume.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import me.noteme.headhunting.domain.resume.entity.ExperienceType;

import java.time.LocalDate;

@Data
public class ExperienceForm {
    @NotEmpty
    private Long resumeId;
    private ExperienceType experienceType;
    private String title;
    private String affiliation;
    private LocalDate startAt;
    private LocalDate endAt;
    private String description;
    private String referenceUrl;
    private Long experienceId;
}
