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

    public static Certification of(Long id, String name, String organization, String credential, LocalDate acquisitionAt, String grade, CertificationType certificationType, Resume resume) {
        Certification certification = new Certification();
        certification.id = id;
        certification.name = name;
        certification.organization = organization;
        certification.credential = credential;
        certification.acquisitionAt = acquisitionAt;
        certification.grade = grade;
        certification.certificationType = certificationType;
        certification.resume = resume;
        return certification;
    }
 }