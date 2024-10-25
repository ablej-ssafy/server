package me.noteme.headhunting.domain.member.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.response.BaseResponse;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.common.service.EmailService;
import me.noteme.headhunting.domain.member.controller.request.SignUpRequest;
import me.noteme.headhunting.domain.member.dto.JwtToken;
import me.noteme.headhunting.domain.member.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/sign-in")
    public SuccessResponse<JwtToken> signIn(
            @Validated @RequestBody SignUpRequest request,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        return SuccessResponse.of(
                authService.signIn(request.getEmail(), request.getPassword())
        );
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Void> signUp(
            @Validated @RequestBody SignUpRequest request,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        authService.signUp(request.getEmail(), request.getPassword(), request.getName());
        return SuccessResponse.empty();
    }

    @GetMapping("/confirm/email/{key}")
    public SuccessResponse<Void> confirmEmail(@PathVariable("key") String key) {
        // TODO: 인증 처리 -> Redis 조회
        authService.verify(key);
        return SuccessResponse.empty();
    }
}
