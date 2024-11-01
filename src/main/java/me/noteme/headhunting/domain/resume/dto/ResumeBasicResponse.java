package me.noteme.headhunting.domain.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.noteme.headhunting.domain.resume.entity.ResumeBasic;

import java.time.LocalDate;

@Data
@AllArgsConstructor(staticName = "of")
public class ResumeBasicResponse {
    private Long resumeId;
    private Long resumeBasicId;
    private String title;
    private String profile;
    private String name;
    private String email;
    private LocalDate birth;
    private String phone;
    private String job;
    private String introduce;
    private String portfolioUrl;

    public static ResumeBasicResponse fromEntity(ResumeBasic basic) {
        return ResumeBasicResponse.of(
                basic.getResume().getId(),
                basic.getId(),
                basic.getTitle(),
                basic.getProfileImage(),
                basic.getName(),
                basic.getEmail(),
                basic.getBirth(),
                basic.getPhone(),
                basic.getJob().getJobTitle(),
                basic.getIntroduce(),
                basic.getPortfolioUrl()
        );
    }
}
