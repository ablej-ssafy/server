package me.noteme.headhunting.domain.resume.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import me.noteme.headhunting.domain.resume.entity.CertificationType;

import java.time.LocalDate;
import java.util.List;

@Data
public class CertificationForm {
    @NotEmpty
    private Long resumeId;
    private String name;
    private String organization;
    private String credential;
    private LocalDate acquisitionAt;
    private String grade;
    private CertificationType certificationType;
    private Long certificationId;
}
