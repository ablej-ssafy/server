package me.noteme.headhunting.domain.member.security;

import me.noteme.headhunting.domain.member.entity.ProviderType;
import me.noteme.headhunting.domain.member.security.response.GoogleResponse;
import me.noteme.headhunting.domain.member.security.response.KakaoResponse;
import me.noteme.headhunting.domain.member.security.response.OAuth2Response;

import java.util.Map;

public class CustomOAuth2UserFactory {
    public static OAuth2Response parseOAuth2Response(ProviderType providerType, Map<String, Object> attributes) {
        System.out.println(attributes);
        return switch (providerType) {
            case KAKAO -> new KakaoResponse(attributes);
            case GOOGLE -> new GoogleResponse(attributes);
            default -> throw new IllegalArgumentException("Unsupported provider type: " + providerType);
        };
    }
}
