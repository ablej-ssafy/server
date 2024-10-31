package me.noteme.headhunting.domain.job.entity;

import jakarta.persistence.*;
import lombok.*;
import me.noteme.headhunting.domain.resume.entity.ResumeBasic;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "job")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long id;

    @Column(name = "job_title")
    String jobTitle;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InterestJob> interestJobs = new ArrayList<>();

    @OneToOne(mappedBy = "job", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ResumeBasic resumeBasic;
}
