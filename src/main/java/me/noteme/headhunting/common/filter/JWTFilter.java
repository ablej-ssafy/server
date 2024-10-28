package me.noteme.headhunting.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.domain.member.security.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    public final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // * API 요청이 아닌 경우 혹은 인증 관련된 API 요청인 경우 필터를 건너뛴다.
        if (!requestURI.startsWith("/api/v1") || requestURI.startsWith("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = resolveToken(request);
        log.debug("accessToken: {}", accessToken);
        try {
            if (accessToken != null) {
                Authentication authentication = jwtTokenProvider.parseAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
            }
        } catch (Exception e) {
            log.error("errors: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            request.setAttribute("error-message", e.getMessage());
        }
        doFilter(request, response, filterChain);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
