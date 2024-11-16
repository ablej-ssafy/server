package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;
import me.noteme.headhunting.common.entity.BaseEntity;
import me.noteme.headhunting.domain.member.entity.Member;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resume")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Resume extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "hash_key", unique = true)
    private String hashKey;

    @Column(name = "is_private")
    @ColumnDefault("false")
    @Builder.Default
    private boolean isPrivate = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "template_type")
    @ColumnDefault("'BASIC_LIGHT'")
    @Builder.Default
    private ResumeTemplateType templateType = ResumeTemplateType.BASIC_LIGHT;

    @OneToOne(mappedBy = "resume", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ResumeBasic resumeBasic;

    /**
     * 자격증 테이블(자격증, 어학)
     */
    @OneToMany(mappedBy = "resume", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Certification> certifications = new ArrayList<>();

    /**
     * 학력 테이블
     */
    @OneToMany(mappedBy = "resume", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Education> educations = new ArrayList<>();

    /**
     * 경험 (직무, 프로젝트, 대내외 활동)
     */
    @OneToMany(mappedBy = "resume", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Experience> experiences = new ArrayList<>();

    /**
     * 기술 스택
     */
    @OneToOne(mappedBy = "resume", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private TechStack techStack;

    public void changeTemplate(ResumeTemplateType templateType) {
        this.templateType = templateType;
    }

    public void updateVisible(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
