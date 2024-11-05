package me.noteme.headhunting.domain.member.dto;

import me.noteme.headhunting.domain.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public record CustomOAuth2User(Member member) implements OAuth2User, UserDetails {
    @Override
    public Map<String, Object> getAttributes() {
        return Map.of("username", member.getUsername(), "authorities", getAuthorities());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> member.getRoleType().name());
        return collection;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    public String getName() {
        return member.getNickname();
    }

    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
//        return !user.isLocked();
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
//        return !user.isLocked();
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
//        return !user.isLocked();
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
