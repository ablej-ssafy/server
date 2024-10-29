package me.noteme.headhunting.domain.resume.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ResumeBasicRequest {
    @NotEmpty(message = "이력서 번호는 필수값입니다.")
    private Long resumeId;
    private Long jobId;
    private String title;
    private String name;
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    private LocalDate birth;
    private String phone;
    private String introduce;
    private String portfolioUrl;
    private Long resumeBasicId;
}
