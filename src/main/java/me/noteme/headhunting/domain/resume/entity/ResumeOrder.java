package me.noteme.headhunting.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "resume_order")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumeOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_order_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "resume_id", foreignKey = @ForeignKey(name = "fk_resume_order_resume"))
    private Resume resume;

    @Column(name = "basic_order")
    @ColumnDefault("0")
    @Builder.Default
    private int basicOrder = 0;

    @Column(name = "education_order")
    @ColumnDefault("1")
    @Builder.Default
    private int educationOrder = 1;

    @Column(name = "company_order")
    @ColumnDefault("2")
    @Builder.Default
    private int companyOrder = 2;

    @Column(name = "project_order")
    @ColumnDefault("3")
    @Builder.Default
    private int projectOrder = 3;

    @Column(name = "activity_order")
    @ColumnDefault("4")
    @Builder.Default
    private int activityOrder = 4;

    @Column(name = "qualification_order")
    @ColumnDefault("5")
    @Builder.Default
    private int qualificationOrder = 5;

    @Column(name = "language_order")
    @ColumnDefault("6")
    @Builder.Default
    private int languageOrder = 6;

    @Column(name = "tech_order")
    @ColumnDefault("7")
    @Builder.Default
    private int techOrder = 7;

    public void update(int education, int company, int project, int activity, int qualification, int language, int tech) {
        this.educationOrder = education;
        this.companyOrder = company;
        this.projectOrder = project;
        this.activityOrder = activity;
        this.qualificationOrder = qualification;
        this.languageOrder = language;
        this.techOrder = tech;
    }
}
