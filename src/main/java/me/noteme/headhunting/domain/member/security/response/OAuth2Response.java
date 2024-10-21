package me.noteme.headhunting.domain.member.security.response;

import me.noteme.headhunting.domain.member.entity.ProviderType;

public interface OAuth2Response {
    ProviderType getProvider();

    String getProviderId();

    String getEmail();

    String getName();

    String getProfileImage();
}
