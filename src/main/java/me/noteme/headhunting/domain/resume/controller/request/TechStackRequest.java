package me.noteme.headhunting.domain.resume.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class TechStackRequest {
    @Min(value = 1, message = "이력서 번호는 필수값입니다.")
    private Long resumeId;
    private List<String> referenceUrls;
    private List<Long> techSkills;
    private Long techStackId;
}
