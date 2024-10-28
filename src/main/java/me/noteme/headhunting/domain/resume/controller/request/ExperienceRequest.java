package me.noteme.headhunting.domain.resume.controller.request;

import lombok.Data;
import me.noteme.headhunting.domain.resume.entity.ExperienceType;

import java.time.LocalDate;

@Data
public class ExperienceRequest {
    private ExperienceType experienceType;
    private String title;
    private String affiliation;
    private LocalDate startAt;
    private LocalDate endAt;
    private String description;
    private String referenceUrl;
}
