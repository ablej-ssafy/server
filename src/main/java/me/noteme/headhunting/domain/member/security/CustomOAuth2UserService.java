package me.noteme.headhunting.domain.member.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.listener.event.ConfirmEmailEvent;
import me.noteme.headhunting.common.listener.event.ResumeInitEvent;
import me.noteme.headhunting.domain.member.dto.CustomOAuth2User;
import me.noteme.headhunting.domain.member.entity.Member;
import me.noteme.headhunting.domain.member.entity.ProviderType;
import me.noteme.headhunting.domain.member.repository.MemberRepository;
import me.noteme.headhunting.domain.member.security.response.OAuth2Response;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final ApplicationEventPublisher publisher;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        ProviderType providerType = ProviderType.valueOf(registrationId.toUpperCase());
        OAuth2Response response = CustomOAuth2UserFactory.parseOAuth2Response(providerType, oauth2User.getAttributes());

        String username = response.getEmail();

        AtomicBoolean isNewUser = new AtomicBoolean(false);
        Member member = memberRepository.findByUsername(username).orElseGet(() -> {
            isNewUser.set(true);
            String encodedPassword = passwordEncoder.encode(response.getProviderId());
            return memberRepository.saveAndFlush(
                    Member.builder()
                            .username(username)
                            .password(encodedPassword)
                            .providerType(providerType)
                            .profileImage(response.getProfileImage())
                            .nickname(response.getName())
                            .emailVerified(true)
                            .build()
            );
        });

        if (isNewUser.get()) {
            publisher.publishEvent(ResumeInitEvent.of(member.getId()));
        }

        return new CustomOAuth2User(member);
    }
}
