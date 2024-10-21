package me.noteme.headhunting.domain.member.controller;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.domain.member.controller.request.SignUpRequest;
import me.noteme.headhunting.domain.member.dto.JwtToken;
import me.noteme.headhunting.domain.member.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

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


}
