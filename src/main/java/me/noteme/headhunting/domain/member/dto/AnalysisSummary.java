package me.noteme.headhunting.domain.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AnalysisSummary {
    @NotEmpty(message = "프로젝트 요약은 필수 값 입니다.")
    private String summation;

    @NotEmpty(message = "사용 기술 목록은 필수 값 입니다.")
    private List<TechSkill> techSkills;

    @NotEmpty(message = "핵심 기능 목록은 필수 값 입니다.")
    private List<KeyFeature> keyFeatures;

    @Data
    public static class TechSkill {
        @NotEmpty(message = "기술 이름은 필수 값 입니다.")
        private String skill;

        @NotEmpty(message = "기술 설명은 필수 값 입니다.")
        private String description;
    }

    @Data
    public static class KeyFeature {
        @NotEmpty(message = "기능 이름은 필수 값 입니다.")
        private String feature;

        @NotEmpty(message = "기능 설명은 필수 값 입니다.")
        private String description;
    }
}

