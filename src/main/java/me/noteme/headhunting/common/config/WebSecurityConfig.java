package me.noteme.headhunting.common.config;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.common.filter.JWTFilter;
import me.noteme.headhunting.domain.member.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final CustomAuthorizationRepository authorizationRepository;
    private final CustomOAuth2UserService oAuth2UserService;
    private final JWTFilter jwtFilter;
    private final CustomSuccessHandler successHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAuthenticationDeniedHandler authenticationDeniedHandler;
    private final String[] permitAllPaths = {
            "/favicon.ico",
            "/api/v1/auth/**",
            "/api/v1/recruitments/**",
            "/api/v1/recruitments",
            "/api/v1/companies/**",
            "/api/v1/search/**",
            "/api/v1/github/**"
    };

    @Bean
    public SecurityFilterChain configure(HttpSecurity security) throws Exception {
        return security
                .cors(configurer -> configurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(authenticationDeniedHandler)
                )
                .oauth2Login((oauth2) -> oauth2.authorizationEndpoint(authorization ->
                                        authorization.baseUri("/oauth2/authorization")
                                                .authorizationRequestRepository(authorizationRepository)
                                ).redirectionEndpoint(redirect -> redirect.baseUri("/oauth2/code/*"))
                                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                                .successHandler(successHandler)
                ).authorizeHttpRequests((auth) -> auth
                        .requestMatchers(permitAllPaths).permitAll()
                        .requestMatchers("/api/v1/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            var cors = new CorsConfiguration();
            cors.setAllowCredentials(true);
            cors.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8080", "https://noteme.kro.kr"));
            cors.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
            cors.setAllowedHeaders(List.of("*"));
            return cors;
        };
    }
}
