package me.noteme.headhunting.domain.resume.entity.mongo;

import jakarta.persistence.Id;
import lombok.*;
import me.noteme.headhunting.domain.resume.entity.Certification;
import me.noteme.headhunting.domain.resume.entity.CertificationType;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(collection = "certification")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MongoCertification {
    @Id
    private String id;

    @Field("certificationId")
    private Long certificationId;

    private String name;

    private String organization;

    private String credential;

    @Field("acquisitionAt")
    private LocalDate acquisitionAt;

    private String grade;

    @Field("certificationType")
    @Builder.Default
    private CertificationType certificationType = CertificationType.QUALIFICATION;

    public static MongoCertification from(Certification certification) {
        return MongoCertification.builder()
                .certificationId(certification.getId())
                .name(certification.getName())
                .organization(certification.getOrganization())
                .credential(certification.getCredential())
                .acquisitionAt(certification.getAcquisitionAt())
                .grade(certification.getGrade())
                .certificationType(certification.getCertificationType())
                .build();
    }
}