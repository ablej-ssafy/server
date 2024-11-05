package me.noteme.headhunting.domain.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.annotation.LoginUser;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.response.SuccessResponse;
import me.noteme.headhunting.common.utils.CookieUtils;
import me.noteme.headhunting.domain.member.controller.request.RefreshRequest;
import me.noteme.headhunting.domain.member.controller.request.EmailRequest;
import me.noteme.headhunting.domain.member.controller.request.SignInRequest;
import me.noteme.headhunting.domain.member.controller.request.SignUpRequest;
import me.noteme.headhunting.domain.member.dto.JwtToken;
import me.noteme.headhunting.domain.member.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static me.noteme.headhunting.domain.member.security.JwtTokenProvider.ACCESS_TOKEN_COOKIE;
import static me.noteme.headhunting.domain.member.security.JwtTokenProvider.REFRESH_TOKEN_COOKIE;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Value("${jwt.access-expire}")
    private Long accessExpire;

    @Value("${jwt.refresh-expire}")
    private Long refreshExpire;

    @PostMapping("/sign-in")
    public SuccessResponse<JwtToken> signIn(
            HttpServletResponse response,
            @Validated @RequestBody SignInRequest request,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        JwtToken token = authService.signIn(request.getEmail(), request.getPassword());

        addToken(response, token);

        return SuccessResponse.of(token);
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Void> signUp(
            @Validated @RequestBody SignUpRequest request,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST, errors);
        }

        authService.signUp(
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                request.getCareerYear(),
                request.getJobIds()
        );
        return SuccessResponse.empty();
    }

    @PostMapping("/sign-out")
    public SuccessResponse<Void> signOut(
            HttpServletRequest request,
            HttpServletResponse response,
            @LoginUser Long memberId,
            @Validated @RequestBody RefreshRequest refreshRequest,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        String refreshToken = parseRefreshToken(request, refreshRequest.getRefreshToken());

        authService.signOut(memberId, refreshToken);

        removeToken(response);
        return SuccessResponse.empty();
    }

    @PostMapping("/refresh")
    public SuccessResponse<JwtToken> refresh(
            HttpServletRequest request,
            HttpServletResponse response,
            @LoginUser Long memberId,
            @Validated @RequestBody RefreshRequest refreshRequest,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        String refreshToken = parseRefreshToken(request, refreshRequest.getRefreshToken());

        JwtToken token = authService.refresh(memberId, refreshToken);
        addToken(response, token);

        return SuccessResponse.of(token);
    }

    @GetMapping("/confirm/email/{key}")
    public SuccessResponse<Void> confirmEmail(@PathVariable("key") String key) {
        authService.verify(key);
        return SuccessResponse.empty();
    }

    @PostMapping("/resend")
    public SuccessResponse<Void> resendEmail(
            @Validated @RequestBody EmailRequest request,
            Errors errors
    ){
        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        authService.resendEmail(request.getEmail());
        return SuccessResponse.empty();
    }

    private String parseRefreshToken(HttpServletRequest request, String refreshToken) {
        return CookieUtils.getCookie(
                request, REFRESH_TOKEN_COOKIE
        ).map(Cookie::getValue).orElse(refreshToken);
    }

    private void addToken(HttpServletResponse response, JwtToken token) {
        CookieUtils.addCookie(
                response, ACCESS_TOKEN_COOKIE, token.getAccessToken(), (int) (accessExpire / 1000), true
        );
        CookieUtils.addCookie(
                response, REFRESH_TOKEN_COOKIE, token.getRefreshToken(), (int) (refreshExpire / 1000), true
        );
    }

    private void removeToken(HttpServletResponse response) {
        CookieUtils.removeCookie(response, ACCESS_TOKEN_COOKIE);
        CookieUtils.removeCookie(response, REFRESH_TOKEN_COOKIE);
    }
}
