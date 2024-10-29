package me.noteme.headhunting.domain.member.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.response.ErrorResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        if (request.getAttribute("error-message") == null) {
            request.setAttribute("error-message", ErrorCode.AUTHENTICATION_FAILED.getMessage());
        }
        ErrorResponse errorResponse = ErrorResponse.of(new CustomException(ErrorCode.AUTHENTICATION_FAILED));
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(errorResponse.toJson());
    }
}