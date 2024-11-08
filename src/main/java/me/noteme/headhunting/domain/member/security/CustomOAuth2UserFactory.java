package me.noteme.headhunting.domain.member.security;

import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.domain.member.entity.ProviderType;
import me.noteme.headhunting.domain.member.security.response.GoogleResponse;
import me.noteme.headhunting.domain.member.security.response.KakaoResponse;
import me.noteme.headhunting.domain.member.security.response.OAuth2Response;

import java.util.Map;

public abstract class CustomOAuth2UserFactory {
    public static OAuth2Response parseOAuth2Response(ProviderType providerType, Map<String, Object> attributes) {
        return switch (providerType) {
            case KAKAO -> new KakaoResponse(attributes);
            case GOOGLE -> new GoogleResponse(attributes);
            default -> throw new CustomException(ErrorCode.BAD_REQUEST, "유효하지 않은 제공자입니다. ");
        };
    }
}
