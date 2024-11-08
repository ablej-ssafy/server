package me.noteme.headhunting.domain.resume.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.noteme.headhunting.domain.resume.entity.Certification;
import me.noteme.headhunting.domain.resume.entity.CertificationType;
import me.noteme.headhunting.domain.resume.entity.Resume;
import org.checkerframework.checker.units.qual.C;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CertificationForm {
    /**
     * 자격증 이름
     */
    private String name;

    /**
     * 발급 기관
     */
    private String organization;

    /**
     * 자격 번호
     */
    private String credential;

    /**
     * 취득 날짜
     */
    @PastOrPresent(message = "현재 또는 과거의 날짜만 가능합니다.")
    private LocalDate acquisitionAt;

    /**
     * 등급
     */
    private String grade;

    /**
     * QUALIFICATION, LANGUAGE
     */
    @NotNull(message = "어학 또는 자격증이 포함되어야 합니다.")
    private CertificationType certificationType;

    private Long certificationId;

    public Certification toEntity(Resume resume) {
        return Certification.of(
                this.certificationId,
                this.name,
                this.organization,
                this.credential,
                this.acquisitionAt,
                this.grade,
                this.certificationType,
                resume
        );
    }

    public static CertificationForm fromEntity(Certification certification) {
        return CertificationForm.of(
                certification.getName(),
                certification.getOrganization(),
                certification.getCredential(),
                certification.getAcquisitionAt(),
                certification.getGrade(),
                certification.getCertificationType(),
                certification.getId()
        );
    }
}
