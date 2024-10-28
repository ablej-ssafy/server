package me.noteme.headhunting.domain.member.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RefreshRequest {
    @NotEmpty(message = "리프레시 토큰은 필수입니다.")
    private String refreshToken;
}
