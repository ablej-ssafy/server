package me.noteme.headhunting.domain.resume.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.noteme.headhunting.domain.resume.entity.Experience;
import me.noteme.headhunting.domain.resume.entity.ExperienceType;
import me.noteme.headhunting.domain.resume.entity.Resume;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ExperienceForm {
    @Min(value = 1, message = "이력서 번호는 필수값입니다.(최소 1)")
    private Long resumeId;

    /**
     * COMPANY, PROJECT, ACTIVITY
     */
    @NotNull(message = "회사, 프로젝트, 대내외활동 등이 작성돼야합니다.")
    private ExperienceType experienceType;

    /**
     * 활동 이름
     */
    private String title;

    /**
     * 활동 소속
     */
    private String affiliation;

    private LocalDate startAt;

    private LocalDate endAt;

    /**
     * 활동 내용 설명
     */
    private String description;

    /**
     * 참조 URL
     */
    private String referenceUrl;

    private Long experienceId;

    public Experience toEntity(Resume resume) {
        return Experience.of(
                this.experienceId,
                this.experienceType,
                this.title,
                this.affiliation,
                this.startAt,
                this.endAt,
                this.description,
                this.referenceUrl,
                resume
        );
    }
}
