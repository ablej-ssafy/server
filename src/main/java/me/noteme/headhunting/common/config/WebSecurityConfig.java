package me.noteme.headhunting.common.config;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.domain.member.security.CustomAuthorizationRepository;
import me.noteme.headhunting.domain.member.security.CustomOAuth2UserService;
import me.noteme.headhunting.domain.member.security.CustomSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final CustomAuthorizationRepository authorizationRepository;
    private final CustomOAuth2UserService oAuth2UserService;
    private final CustomSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain configure(HttpSecurity security) throws Exception {
        return security
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth.anyRequest().permitAll())
                .oauth2Login((oauth2) -> oauth2.authorizationEndpoint(authorization ->
                                        authorization.baseUri("/oauth2/authorization")
                                                .authorizationRequestRepository(authorizationRepository)
                                ).redirectionEndpoint(redirect -> redirect.baseUri("/oauth2/code/*"))
                                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                                .successHandler(successHandler)
                )
                .build();
    }
}
