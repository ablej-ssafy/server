package me.noteme.headhunting.domain.recruitment.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "job_category"
)
public class JobCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_category_id")
    private Long id;

    private String name;
}
