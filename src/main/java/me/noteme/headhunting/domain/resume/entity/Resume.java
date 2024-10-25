package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.noteme.headhunting.common.entity.BaseEntity;
import me.noteme.headhunting.domain.job.entity.Job;
import me.noteme.headhunting.domain.member.entity.Member;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    private String title;

    @Column(name = "profile_image")
    private String profileImage;

    private String name;

    private String email;

    private String birth;

    private String phone;

    private String introduce;

    @Column(name = "portfolio_url")
    private String portfolioUrl;

}
