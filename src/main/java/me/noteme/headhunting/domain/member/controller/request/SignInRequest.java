package me.noteme.headhunting.domain.member.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignInRequest {
    @NotEmpty(message = "아이디는 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
}
