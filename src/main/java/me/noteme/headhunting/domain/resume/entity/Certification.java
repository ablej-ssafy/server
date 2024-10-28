package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private LocalDateTime acquisitionAt;

    private String grade;

    @Enumerated(EnumType.STRING)
    @Column(name = "certification_type")
    private CertificationType certificationType;
}