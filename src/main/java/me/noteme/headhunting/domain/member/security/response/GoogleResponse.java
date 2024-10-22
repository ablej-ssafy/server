package me.noteme.headhunting.domain.member.security.response;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.domain.member.entity.ProviderType;

import java.util.Map;

@RequiredArgsConstructor
public class GoogleResponse implements OAuth2Response {
    private final Map<String, Object> attribute;

    @Override
    public ProviderType getProvider() {
        return ProviderType.GOOGLE;
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }

    @Override
    public String getProfileImage() {
        return attribute.get("picture").toString();
    }
}
