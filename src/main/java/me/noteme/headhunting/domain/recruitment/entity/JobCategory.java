package me.noteme.headhunting.domain.recruitment.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "job_category")
public class JobCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_category_id")
    private Long id;

    private String name;

    public static final Set<Long> mainCategoryIds = new HashSet<>(Set.of(
            1L, 6L, 10L, 21L, 45L, 51L, 56L, 67L, 78L, 83L,
            113L, 120L, 122L, 126L, 129L, 134L, 144L, 163L, 280L
    ));
}
