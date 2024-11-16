package me.noteme.headhunting.domain.resume.controller.request;

import lombok.Data;
import me.noteme.headhunting.domain.resume.entity.ResumeTemplateType;

@Data
public class TemplateRequest {
    private ResumeTemplateType templateType;
}
