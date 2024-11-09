package me.noteme.headhunting.domain.resume.entity.mongo;

import lombok.*;
import me.noteme.headhunting.domain.resume.entity.ResumeBasic;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "basic")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MongoResumeBasic {
    private String id;
    private String title;
    private String name;
    private String email;
    private LocalDate birth;
    private String phone;
    private String introduce;
    private String portfolioUrl;
    private String job;
    private String profileImage;

    public static MongoResumeBasic from(ResumeBasic resumeBasic) {
        return MongoResumeBasic.builder()
                .title(resumeBasic.getTitle())
                .name(resumeBasic.getName())
                .email(resumeBasic.getEmail())
                .birth(resumeBasic.getBirth())
                .phone(resumeBasic.getPhone())
                .introduce(resumeBasic.getIntroduce())
                .portfolioUrl(resumeBasic.getPortfolioUrl())
                .job(resumeBasic.getJob())
                .profileImage(resumeBasic.getProfileImage())
                .build();
    }
}