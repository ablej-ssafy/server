package me.noteme.headhunting.domain.member.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.service.EmailService;
import me.noteme.headhunting.domain.member.controller.request.SignUpRequest;
import me.noteme.headhunting.domain.member.dto.JwtToken;
import me.noteme.headhunting.domain.member.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/sign-in")
    public JwtToken signIn(
            @Validated @RequestBody SignUpRequest request,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            // TODO: 에러 처리
        }

        return authService.signIn(request.getEmail(), request.getPassword());
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(
            @Validated @RequestBody SignUpRequest request,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            // TODO: 에러 처리
        }

        authService.signUp(request.getEmail(), request.getPassword(), request.getName());
    }

    @GetMapping("/test")
    public void test() {
        emailService.sendConfirmationEmail("akys159357@naver.com", "김용수","123123");
    }

    @GetMapping("/confirm/email/{key}")
    public void confirmEmail(@PathVariable("key") String key, HttpServletResponse response) throws IOException {
        log.debug("인증 완료 {}",key);
        // TODO: 인증 처리 -> Redis 조회
        response.sendRedirect("/");
    }
}
