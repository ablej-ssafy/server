package me.noteme.headhunting.domain.resume.controller.request;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ResumeBasicRequest {
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
     * 작성자 핸드폰 (010-1111-2222)
     */
    @Length(max = 13)
    private String phone;

    /**
     * 직무
     */
    private String job;

    /**
     * 한 줄 소개
     */
    private String introduce;

    /**
     * 포트폴리오 URL
     */
    private String portfolioUrl;
}
