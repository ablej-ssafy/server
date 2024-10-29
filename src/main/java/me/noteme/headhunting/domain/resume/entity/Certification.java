package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "certification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    private String name;

    private String organization;

    private String credential;

    @Column(name = "acquisition_at")
    private LocalDate acquisitionAt;

    private String grade;

    @Enumerated(EnumType.STRING)
    @Column(name = "certification_type")
    @Builder.Default
    private CertificationType certificationType = CertificationType.QUALIFICATION;

    public void updateCertification(String name, String organization, String credential, LocalDate acquisitionAt, String grade, CertificationType certificationType) {
        this.name = name;
        this.organization = organization;
        this.credential = credential;
        this.acquisitionAt = acquisitionAt;
        this.grade = grade;
        this.certificationType = certificationType;
    }
 }