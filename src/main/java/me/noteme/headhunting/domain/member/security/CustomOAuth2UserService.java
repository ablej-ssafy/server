package me.noteme.headhunting.domain.member.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.domain.member.dto.CustomOAuth2User;
import me.noteme.headhunting.domain.member.entity.ProviderType;
import me.noteme.headhunting.domain.member.security.response.OAuth2Response;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        ProviderType providerType = ProviderType.valueOf(registrationId.toUpperCase());
        OAuth2Response response = CustomOAuth2UserFactory.parseOAuth2Response(providerType, oauth2User.getAttributes());

        String username = response.getName() + "_" + response.getProviderId();

        AtomicBoolean isNewUser = new AtomicBoolean(false);
//        User user = loadUserPort.loadUser(username).orElseGet(() -> {
//            log.info("신규 유저 생성 응답: {}", response);
//
//            isNewUser.set(true);
//            String encodedPassword = passwordEncoder.encode(response.getProviderId());
//            UserEntity entity = saveUserPort.save(
//                    username,
//                    encodedPassword,
//                    response.getEmail(),
//                    response.getProviderId(),
//                    response.getProfileImage(),
//                    githubToken.getTokenValue()
//            );
//            return UserEntityMapper.toUser(entity);
//        });


        return new CustomOAuth2User(null);
    }
}
