package me.noteme.headhunting.domain.resume.entity.mongo;

import jakarta.persistence.Id;
import lombok.*;
import me.noteme.headhunting.domain.resume.entity.ResumeTemplateType;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "resume")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class MongoResume {
    @Id
    private String id;

    @Indexed(unique = true)
    private Long memberId;

    @Indexed(unique = true)
    private String hashKey;

    private boolean isPrivate;

    private ResumeTemplateType templateType;

    @Field("basic")
    private MongoResumeBasic basic;

    @Field("companies")
    private List<MongoExperience> companies;

    @Field("activities")
    private List<MongoExperience> activities;

    @Field("projects")
    private List<MongoExperience> projects;

    @Field("educations")
    private List<MongoEducation> educations;

    @Field("qualifications")
    private List<MongoCertification> qualifications;

    @Field("languages")
    private List<MongoCertification> languages;
}
