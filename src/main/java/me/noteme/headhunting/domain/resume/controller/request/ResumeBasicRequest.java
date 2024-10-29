package me.noteme.headhunting.domain.resume.controller.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ResumeBasicRequest {
    private Long resumeId;
    private Long jobId;
    private String title;
    private String name;
    private String email;
    private LocalDate birth;
    private String phone;
    private String introduce;
    private String portfolioUrl;
    private Long resumeBasicId;
}
