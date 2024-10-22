package me.noteme.headhunting.domain.member.security.response;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.domain.member.entity.ProviderType;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {
    private final Map<String, Object> attribute;
    private final Map<String, Object> properties;

    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
        this.properties = (Map<String, Object>) attribute.get("properties");
    }

    @Override
    public ProviderType getProvider() {
        return ProviderType.KAKAO;
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        if (properties.get("email") != null) {
            return properties.get("email").toString();
        }
        return getProviderId() + "@kakao.com";
    }

    @Override
    public String getName() {
        return properties.get("nickname").toString();
    }

    @Override
    public String getProfileImage() {
        return properties.get("profile_image").toString();
    }
}
