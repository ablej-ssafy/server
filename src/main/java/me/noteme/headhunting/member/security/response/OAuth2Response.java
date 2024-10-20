package me.noteme.headhunting.member.security.response;

import me.noteme.headhunting.member.entity.ProviderType;

public interface OAuth2Response {
    ProviderType getProvider();

    String getProviderId();

    String getEmail();

    String getName();

    String getProfileImage();
}
