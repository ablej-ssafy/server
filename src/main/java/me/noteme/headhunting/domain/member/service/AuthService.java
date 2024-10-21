package me.noteme.headhunting.domain.member.service;

import lombok.RequiredArgsConstructor;
import me.noteme.headhunting.domain.member.repository.MemberRepository;
import me.noteme.headhunting.domain.member.security.JwtTokenProvider;
import me.noteme.headhunting.domain.member.dto.JwtToken;
import me.noteme.headhunting.domain.member.entity.Member;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

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
    }

    public JwtToken signIn(String email, String password) {
        Member member = memberRepository.findByUsername(email).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER")
        );

        return tokenProvider.generate(member.getId(), authorities);
    }
}
