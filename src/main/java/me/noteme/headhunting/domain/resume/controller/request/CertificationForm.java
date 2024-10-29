package me.noteme.headhunting.domain.resume.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import me.noteme.headhunting.domain.resume.entity.CertificationType;

import java.time.LocalDate;

@Data
public class CertificationForm {
    @NotEmpty(message = "이력서 번호는 필수값입니다.")
    private Long resumeId;
    private String name;
    private String organization;
    private String credential;
    @PastOrPresent(message = "현재 또는 과거의 날짜만 가능합니다.")
    private LocalDate acquisitionAt;
    private String grade;
    @NotNull(message = "어학 또는 자격증이 포함되어야 합니다.")
    private CertificationType certificationType;
    private Long certificationId;
}
