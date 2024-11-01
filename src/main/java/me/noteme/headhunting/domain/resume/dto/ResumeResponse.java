package me.noteme.headhunting.domain.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.noteme.headhunting.domain.resume.controller.request.CertificationForm;
import me.noteme.headhunting.domain.resume.controller.request.EducationalForm;
import me.noteme.headhunting.domain.resume.controller.request.ExperienceForm;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
public class ResumeResponse {
    private ResumeBasicResponse basic;
    private List<EducationalForm> educationals;
    private List<ExperienceForm> companies;
    private List<ExperienceForm> activities;
    private List<ExperienceForm> projects;
    private List<CertificationForm> qualifications;
    private List<CertificationForm> languages;
    private TechResponse tech;
}
