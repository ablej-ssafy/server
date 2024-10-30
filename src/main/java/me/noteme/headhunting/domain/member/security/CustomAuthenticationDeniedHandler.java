package me.noteme.headhunting.domain.member.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.response.ErrorResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ErrorResponse errorResponse = ErrorResponse.of(new CustomException(ErrorCode.ACCESS_DENIED));
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(errorResponse.toJson());
    }
}
