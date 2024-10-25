package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    private String name;

    private String organization;

    private String credential;

    private String grade;

    @Column(name = "acquisition_at")
    private LocalDateTime acquisitionAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "certification_type")
    private CertificationType certificationType;
}