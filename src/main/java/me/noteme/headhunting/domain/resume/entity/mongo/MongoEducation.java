package me.noteme.headhunting.domain.resume.entity.mongo;

import jakarta.persistence.Id;
import lombok.*;
import me.noteme.headhunting.domain.resume.entity.Education;
import me.noteme.headhunting.domain.resume.entity.EducationType;
import me.noteme.headhunting.domain.resume.entity.GradeType;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(collection = "education")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MongoEducation {
    @Id
    private String id;

    @Field("educationId")
    private Long educationId;

    private String name;

    private String major;

    @Field("category")
    @Builder.Default
    private EducationType category = EducationType.BACHELOR;

    private String grade;

    @Field("gradeType")
    @Builder.Default
    private GradeType gradeType = GradeType.FOUR_POINT_FIVE;

    private String description;

    @Field("startAt")
    private LocalDate startAt;

    @Field("endAt")
    private LocalDate endAt;

    public static MongoEducation from(Education education) {
        return MongoEducation.builder()
                .educationId(education.getId())
                .name(education.getName())
                .major(education.getMajor())
                .category(education.getCategory())
                .grade(education.getGrade())
                .gradeType(education.getGradeType())
                .description(education.getDescription())
                .startAt(education.getStartAt())
                .endAt(education.getEndAt())
                .build();
    }
}