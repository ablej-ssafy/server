package me.noteme.headhunting.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import me.noteme.headhunting.domain.recruitment.entity.JobCategory;

@Entity
@Table(name = "interest_job")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterestJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interset_job_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private JobCategory jobCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static InterestJob of (JobCategory job, Member member) {
        InterestJob interestJob = new InterestJob();
        interestJob.jobCategory = job;
        interestJob.member = member;
        return interestJob;
    }
}
