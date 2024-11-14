package me.noteme.headhunting.domain.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import me.noteme.headhunting.domain.resume.entity.CertificationType;
import me.noteme.headhunting.domain.resume.entity.EducationType;
import me.noteme.headhunting.domain.resume.entity.ExperienceType;
import me.noteme.headhunting.domain.resume.entity.GradeType;

import java.time.LocalDate;
import java.util.List;

@ToString
@Data
public class OpenAiResponse {
    private AiBasic aiBasic;
    private List<AiEducational> aiEducationals;
    private List<AiExperience> aiExperiences;
    private List<AiCertification> aiCertifications;

    @Data
    @AllArgsConstructor
    public static class AiBasic {
        private String title;
        private String name;
        private String email;
        private LocalDate birth;
        private String phone;
        private String job;
        private String introduce;
        private String portfolioUrl;
    }

    @Data
    @AllArgsConstructor
    public static class AiEducational {
        private String name;
        private String major;
        private EducationType category;
        private Double grade;
        private GradeType gradeType;
        private String description;
        private LocalDate startAt;
        private LocalDate endAt;
    }

    @Data
    @AllArgsConstructor
    public static class AiExperience {
        private ExperienceType experienceType;
        private String title;
        private String affiliation;
        private LocalDate startAt;
        private LocalDate endAt;
        private String description;
        private String referenceUrl;
    }

    @Data
    @AllArgsConstructor
    public static class AiCertification {
        private String name;
        private String organization;
        private String credential;
        private LocalDate acquisitionAt;
        private String grade;
        private CertificationType certificationType;
    }
}
