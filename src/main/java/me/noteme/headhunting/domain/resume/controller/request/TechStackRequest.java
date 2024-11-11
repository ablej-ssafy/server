package me.noteme.headhunting.domain.resume.controller.request;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TechStackRequest {
    private Long techId;
    private String githubUrl;
    private String notionUrl;
    private List<Long> techSkills;
}
