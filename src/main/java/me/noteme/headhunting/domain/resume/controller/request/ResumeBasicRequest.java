package me.noteme.headhunting.domain.resume.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class ResumeBasicRequest {
    @Min(value = 1, message = "이력서 번호는 필수값입니다.")
    private Long resumeId;
    private Long jobId;
    /**
     * 이력서 프로필 URL
     */
    private String profile;
    /**
     * 이력서 제목
     */
    private String title;
    /**
     * 작성자 이름
     */
    private String name;
    /**
     * 작성자 이메일
     */
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    /**
     * 작성자 생년월일 (1999-10-29)
     */
    private LocalDate birth;
    /**
     * 작성자 핸드폰 (01011112222)
     */
    @Length(max = 11)
    private String phone;
    /**
     * 한 줄 소개
     */
    private String introduce;
    /**
     * 포트폴리오 URL
     */
    private String portfolioUrl;
    private Long resumeBasicId;
}
