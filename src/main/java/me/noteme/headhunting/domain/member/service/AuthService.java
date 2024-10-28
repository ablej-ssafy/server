package me.noteme.headhunting.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import me.noteme.headhunting.common.listener.event.SignUpEvent;
import me.noteme.headhunting.common.service.EmailService;
import me.noteme.headhunting.domain.member.repository.MemberRepository;
import me.noteme.headhunting.domain.member.security.JwtTokenProvider;
import me.noteme.headhunting.domain.member.dto.JwtToken;
import me.noteme.headhunting.domain.member.entity.Member;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final ApplicationEventPublisher publisher;

    /**
     * 회원 가입 로직
     *
     * @param email    이메일
     * @param password 비밀번호
     * @param name     이름
     */
    @Transactional
    public void signUp(String email, String password, String name) {
        if (memberRepository.findByUsername(email).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);

        Member member = Member.builder()
                .username(email)
                .password(encodedPassword)
                .nickname(name)
                .build();

        memberRepository.save(member);

        publisher.publishEvent(SignUpEvent.of(email, name));
    }

    /**
     * 이메일 인증
     *
     * @param key 이메일 인증 키
     */
    @Transactional
    public void verify(String key) {
        // TODO: 키를 통해 이메일을 가져온다.
        String email = key;

        // TODO: 해당 키가 존재하지않는다면 이메일 만료
        if (Objects.isNull(email)) {
            throw new CustomException(ErrorCode.EXPIRED_URL, "이미 만료된 링크입니다.");
        }

        Member member = getMember(email);
        if (member.isEmailVerified()) {
            throw new CustomException(ErrorCode.EXPIRED_URL, "이미 처리된 사용자입니다.");
        }

        member.verify();
    }

    public JwtToken signIn(String email, String password) {
        Member member = getMember(email);

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER")
        );

        return tokenProvider.generate(member.getId(), authorities);
    }

    private Member getMember(String email) {
        return memberRepository.findByUsername(email)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 사용자입니다."));
    }
}
