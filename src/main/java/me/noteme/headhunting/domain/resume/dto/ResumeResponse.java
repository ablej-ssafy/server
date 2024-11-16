package me.noteme.headhunting.domain.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.noteme.headhunting.domain.resume.controller.request.CertificationForm;
import me.noteme.headhunting.domain.resume.controller.request.EducationForm;
import me.noteme.headhunting.domain.resume.controller.request.ExperienceForm;
import me.noteme.headhunting.domain.resume.entity.Resume;
import me.noteme.headhunting.domain.resume.entity.ResumeTemplateType;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class ResumeResponse {
    private String hashKey;
    private boolean isPrivate;
    private ResumeTemplateType templateType;

    public static ResumeResponse fromEntity(Resume resume) {
        return ResumeResponse.of(
                resume.getHashKey(),
                resume.isPrivate(),
                resume.getTemplateType()
        );
    }
}
