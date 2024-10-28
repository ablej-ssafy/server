package me.noteme.headhunting.domain.member.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class EmailRequest {
    @NotEmpty(message = "아이디는 필수 입력 값입니다.")
    private String email;
}
