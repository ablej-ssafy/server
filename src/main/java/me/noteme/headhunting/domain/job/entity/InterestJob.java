package me.noteme.headhunting.domain.job.entity;

import jakarta.persistence.*;
import lombok.*;
import me.noteme.headhunting.domain.member.entity.Member;

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
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static InterestJob of (Job job, Member member) {
        InterestJob interestJob = new InterestJob();
        interestJob.job = job;
        interestJob.member = member;
        return interestJob;
    }
}
