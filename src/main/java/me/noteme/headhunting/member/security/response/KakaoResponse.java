package me.noteme.headhunting.member.security.response;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.member.entity.ProviderType;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoResponse implements OAuth2Response {
    private final Map<String, Object> attribute;

    @Override
    public ProviderType getProvider() {
        return ProviderType.KAKAO;
    }

    @Override
    public String getProviderId() {
        return "";
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getProfileImage() {
        return "";
    }
}
