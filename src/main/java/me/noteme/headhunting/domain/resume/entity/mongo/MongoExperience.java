package me.noteme.headhunting.domain.resume.entity.mongo;

import jakarta.persistence.Id;
import lombok.*;
import me.noteme.headhunting.domain.resume.entity.Experience;
import me.noteme.headhunting.domain.resume.entity.ExperienceType;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(collection = "experience")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MongoExperience {
    @Id
    private String id;

    @Field("experienceId")
    private Long experienceId;

    @Field("experienceType")
    @Builder.Default
    private ExperienceType experienceType = ExperienceType.PROJECT;

    private String title;

    private String affiliation;

    @Field("startAt")
    private LocalDate startAt;

    @Field("endAt")
    private LocalDate endAt;

    private String description;

    @Field("referenceUrl")
    private String referenceUrl;

    public static MongoExperience from(Experience experience) {
        return MongoExperience.builder()
                .experienceId(experience.getId())
                .experienceType(experience.getExperienceType())
                .title(experience.getTitle())
                .affiliation(experience.getAffiliation())
                .startAt(experience.getStartAt())
                .endAt(experience.getEndAt())
                .description(experience.getDescription())
                .referenceUrl(experience.getReferenceUrl())
                .build();
    }
}