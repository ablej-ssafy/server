package me.noteme.headhunting.domain.job.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "title")
    private String title;
}
