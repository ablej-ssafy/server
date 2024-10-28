package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;
import me.noteme.headhunting.common.entity.BaseEntity;
import me.noteme.headhunting.domain.job.entity.Job;
import me.noteme.headhunting.domain.member.entity.Member;

import java.time.LocalDate;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    /**
     * 이력서 제목
     */
    private String title;

    /**
     * 이력서에 해당하는 프로필 이미지 주소
     */
    @Column(name = "profile_image")
    private String profileImage;

    /**
     * 이름
     */
    private String name;

    /**
     * 해당 사용자의 이메일
     */
    private String email;

    /**
     * 생년월일
     */
    private LocalDate birth;

    /**
     * 핸드폰 번호
     */
    @Column(length = 11)
    private String phone;

    /**
     * 한줄 소개
     */
    private String introduce;

    /**
     * 포트폴리오 URL
     */
    @Column(name = "portfolio_url")
    private String portfolioUrl;

    ///////////////////////////

    /**
     * 자격증 테이블(자격증, 어학)
     */
    @OneToMany(mappedBy = "resume", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certification> certifications;

    /**
     * 학력 테이블
     */
    @OneToMany(mappedBy = "resume", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Educational> educationals;

    /**
     * 경험 (직무, 프로젝트, 대내외 활동)
     */
    @OneToMany(mappedBy = "resume", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences;

    /**
     * 기술 스택
     */
    @OneToOne(mappedBy = "resume", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private TechStack techStack;
}
