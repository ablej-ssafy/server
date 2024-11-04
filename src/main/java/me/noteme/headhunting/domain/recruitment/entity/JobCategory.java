package me.noteme.headhunting.domain.recruitment.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(
        name = "job_category"
)
@Getter
public class JobCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_category_id")
    private Long id;

    private String name;
}
