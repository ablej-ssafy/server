package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;
import me.noteme.headhunting.domain.recruitment.entity.JobCategory;

import java.time.LocalDate;

@Entity
@Table(name = "resume_basic")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ResumeBasic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_basic_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private JobCategory job;

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
    @Column(length = 13)
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

    public static ResumeBasic of(Long id, String title, String name, String email, LocalDate birth, String phone, String introduce, String portfolioUrl, Resume resume, JobCategory jobCategory, String profileImage) {
        return ResumeBasic.builder()
                .id(id)
                .title(title)
                .name(name)
                .email(email)
                .birth(birth)
                .phone(phone)
                .introduce(introduce)
                .portfolioUrl(portfolioUrl)
                .resume(resume)
                .job(jobCategory)
                .profileImage(profileImage)
                .build();
    }
}
