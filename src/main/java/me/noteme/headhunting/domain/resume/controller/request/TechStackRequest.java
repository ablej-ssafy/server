package me.noteme.headhunting.domain.resume.controller.request;

import lombok.Data;

import java.util.List;

@Data
public class TechStackRequest {
    private Long resumeId;
    private List<String> referenceUrls;
    private List<Long> techSkills;
    private Long techStackId;
}
