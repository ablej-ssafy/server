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
        if (attribute.containsKey("kakao_account")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
            return kakaoAccount.get("email").toString();
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
