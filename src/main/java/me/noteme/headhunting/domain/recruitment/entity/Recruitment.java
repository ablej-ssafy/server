package me.noteme.headhunting.domain.recruitment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(
        name = "recruitment"
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruitment_id")
    private Long id;

    @Column(name = "wanted_id")
    private Long wantedId;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recruitment_category_id", foreignKey = @ForeignKey(name = "fk_company_recruitment_category"))
    private JobCategory category;

    @OneToMany(mappedBy = "recruitment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private Set<RecruitmentCategory> childCategories = new HashSet<>();

    @OneToMany(mappedBy = "recruitment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private Set<RecruitmentImage> images = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id", foreignKey = @ForeignKey(name = "fk_recruitment_company"))
    private Company company;

    @Column(name = "thumbnail", length = 500)
    private String thumbnail;

    @Column(name = "intro", columnDefinition = "TEXT")
    private String intro;

    @Column(name = "task", columnDefinition = "TEXT")
    private String task;

    @Column(name = "requirement", columnDefinition = "TEXT")
    private String requirement;

    @Column(name = "preference", columnDefinition = "TEXT")
    private String preference;

    @Column(name = "benefit", columnDefinition = "TEXT")
    private String benefit;

    @Column(name = "hire_round", columnDefinition = "TEXT")
    private String hireRound;

    @Column(name = "due_time")
    private LocalDate dueTime;

    @Column(name = "annual_to")
    @Builder.Default
    private int annualTo = 0;

    @Column(name = "annual_from")
    @Builder.Default
    private int annualFrom = 0;
}
