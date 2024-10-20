package me.noteme.headhunting.member.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignInRequest {
    @NotEmpty(message = "아이디는 필수 입력 값입니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
}
