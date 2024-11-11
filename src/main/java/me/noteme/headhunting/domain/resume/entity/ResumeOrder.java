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
    @ColumnDefault("0.0")
    @Builder.Default
    private double basicOrder = 0.0;

    @Column(name = "education_order")
    @ColumnDefault("1.0")
    @Builder.Default
    private double educationOrder = 1.0;

    @Column(name = "company_order")
    @ColumnDefault("2.0")
    @Builder.Default
    private double companyOrder = 2.0;

    @Column(name = "project_order")
    @ColumnDefault("3.0")
    @Builder.Default
    private double projectOrder = 3.0;

    @Column(name = "activity_order")
    @ColumnDefault("4.0")
    @Builder.Default
    private double activityOrder = 4.0;

    @Column(name = "qualification_order")
    @ColumnDefault("5.0")
    @Builder.Default
    private double qualificationOrder = 5.0;

    @Column(name = "language_order")
    @ColumnDefault("6.0")
    @Builder.Default
    private double languageOrder = 6.0;

    @Column(name = "tech_order")
    @ColumnDefault("7.0")
    @Builder.Default
    private double techOrder = 7.0;

    public void update(String keyword, double value) {
        switch (keyword) {
            case "basic" -> this.basicOrder = value;
            case "education" -> this.educationOrder = value;
            case "company" -> this.companyOrder = value;
            case "project" -> this.projectOrder = value;
            case "activity" -> this.activityOrder = value;
            case "qualification" -> this.qualificationOrder = value;
            case "language" -> this.languageOrder = value;
            case "tech" -> this.techOrder = value;
            default -> throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }
}
